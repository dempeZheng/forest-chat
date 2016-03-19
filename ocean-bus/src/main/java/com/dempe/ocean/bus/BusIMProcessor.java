/*
 * Copyright (c) 2012-2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package com.dempe.ocean.bus;


import com.alibaba.fastjson.JSONArray;
import com.dempe.ocean.client.service.IMServiceClient;
import com.dempe.ocean.common.model.User;
import com.dempe.ocean.common.protocol.mqtt.AbstractMessage.QOSType;
import com.dempe.ocean.common.protocol.mqtt.*;
import com.dempe.ocean.core.BrokerInterceptor;
import com.dempe.ocean.core.ConnectionDescriptor;
import com.dempe.ocean.core.NettyUtils;
import com.dempe.ocean.core.spi.ClientSession;
import com.dempe.ocean.core.spi.IMessagesStore;
import com.dempe.ocean.core.spi.ISessionsStore;
import com.dempe.ocean.core.spi.impl.subscriptions.Subscription;
import com.dempe.ocean.core.spi.impl.subscriptions.SubscriptionsStore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Class responsible to handle the logic of MQTT protocol it's the director of
 * the protocol execution.
 * <p/>
 * Used by the front facing class SimpleMessaging.
 *
 * @author andrea
 */
public class BusIMProcessor {

    static final class WillMessage {
        private final String topic;
        private final ByteBuffer payload;
        private final boolean retained;
        private final QOSType qos;

        public WillMessage(String topic, ByteBuffer payload, boolean retained, QOSType qos) {
            this.topic = topic;
            this.payload = payload;
            this.retained = retained;
            this.qos = qos;
        }

        public String getTopic() {
            return topic;
        }

        public ByteBuffer getPayload() {
            return payload;
        }

        public boolean isRetained() {
            return retained;
        }

        public QOSType getQos() {
            return qos;
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(BusIMProcessor.class);

    protected static ConcurrentMap<String, ConnectionDescriptor> m_clientIDs;
    private boolean allowAnonymous;
    private IMessagesStore m_messagesStore;
    private ISessionsStore m_sessionsStore;
    private BrokerInterceptor m_interceptor;
    private IMServiceClient imServiceClient;

    //maps clientID to Will testament, if specified on CONNECT
    private ConcurrentMap<String, WillMessage> m_willStore = new ConcurrentHashMap<>();


    /**
     *                        clients subscriptions.
     * @param storageService  the persistent store to use for save/load of messages
     *                        for QoS1 and QoS2 handling.
     * @param sessionsStore   the clients sessions store, used to persist subscriptions.
     * @param allowAnonymous  true connection to clients without credentials.
     * @param imServiceClient used to apply ACL policies to publishes and subscriptions.
     * @param interceptor     to notify events to an intercept handler
     */
    void init( IMessagesStore storageService,
              ISessionsStore sessionsStore,
              boolean allowAnonymous, IMServiceClient imServiceClient, BrokerInterceptor interceptor) {
        this.m_clientIDs = new ConcurrentHashMap<>();
        this.m_interceptor = interceptor;
        this.allowAnonymous = allowAnonymous;
        this.imServiceClient = imServiceClient;
        m_messagesStore = storageService;
        m_sessionsStore = sessionsStore;
    }

    public void processConnect(Channel channel, ConnectMessage msg) throws Exception {
        LOG.debug("CONNECT for client <{}>", msg.getClientID());
        if (msg.getProtocolVersion() != 3 && msg.getProtocolVersion() != 4) {
            ConnAckMessage badProto = new ConnAckMessage();
            badProto.setReturnCode(ConnAckMessage.UNNACEPTABLE_PROTOCOL_VERSION);
            LOG.warn("processConnect sent bad proto ConnAck");
            channel.writeAndFlush(badProto);
            channel.close();
            return;
        }

        if (msg.getClientID() == null || msg.getClientID().length() == 0) {
            ConnAckMessage okResp = new ConnAckMessage();
            okResp.setReturnCode(ConnAckMessage.IDENTIFIER_REJECTED);
            channel.writeAndFlush(okResp);
            m_interceptor.notifyClientConnected(msg);
            return;
        }

        //handle user authentication
        if (msg.isUserFlag()) {
            byte[] pwd = null;
            if (msg.isPasswordFlag()) {
                pwd = msg.getPassword();
            } else if (!this.allowAnonymous) {
                failedCredentials(channel);
                return;
            }
            // 处理登录问题
            User user = imServiceClient.login(Long.valueOf(msg.getUsername()), new String(pwd));
            // 如果登录失败
            if (user == null) {
                failedCredentials(channel);
                channel.close();
                return;
            }
            NettyUtils.userName(channel, msg.getUsername());

        } else if (!this.allowAnonymous) {
            failedCredentials(channel);
            return;
        }

        //if an old client with the same ID already exists close its session.
        if (m_clientIDs.containsKey(msg.getClientID())) {
            LOG.info("Found an existing connection with same client ID <{}>, forcing to close", msg.getClientID());
            //clean the subscriptions if the old used a cleanSession = true
            Channel oldChannel = m_clientIDs.get(msg.getClientID()).channel;
            NettyUtils.sessionStolen(oldChannel, true);
            oldChannel.close();
            LOG.debug("Existing connection with same client ID <{}>, forced to close", msg.getClientID());
        }

        ConnectionDescriptor connDescr = new ConnectionDescriptor(msg.getClientID(), channel, msg.isCleanSession());
        m_clientIDs.put(msg.getClientID(), connDescr);

        int keepAlive = msg.getKeepAlive();
        LOG.debug("Connect with keepAlive {} s", keepAlive);
        NettyUtils.keepAlive(channel, keepAlive);
        //session.attr(NettyUtils.ATTR_KEY_CLEANSESSION).set(msg.isCleanSession());
        NettyUtils.cleanSession(channel, msg.isCleanSession());
        //used to track the client in the subscription and publishing phases.
        //session.attr(NettyUtils.ATTR_KEY_CLIENTID).set(msg.getClientID());
        NettyUtils.clientID(channel, msg.getClientID());
        LOG.debug("Connect create session <{}>", channel);

        setIdleTime(channel.pipeline(), Math.round(keepAlive * 1.5f));

        //Handle will flag
        if (msg.isWillFlag()) {
            QOSType willQos = QOSType.valueOf(msg.getWillQos());
            byte[] willPayload = msg.getWillMessage();
            ByteBuffer bb = (ByteBuffer) ByteBuffer.allocate(willPayload.length).put(willPayload).flip();
            //save the will testament in the clientID store
            WillMessage will = new WillMessage(msg.getWillTopic(), bb, msg.isWillRetain(), willQos);
            m_willStore.put(msg.getClientID(), will);
        }

        ConnAckMessage okResp = new ConnAckMessage();
        okResp.setReturnCode(ConnAckMessage.CONNECTION_ACCEPTED);

        LOG.info("CONNECT processed");
    }

    private void setIdleTime(ChannelPipeline pipeline, int idleTime) {
        if (pipeline.names().contains("idleStateHandler")) {
            pipeline.remove("idleStateHandler");
        }
        pipeline.addFirst("idleStateHandler", new IdleStateHandler(0, 0, idleTime));
    }

    private void failedCredentials(Channel session) {
        ConnAckMessage okResp = new ConnAckMessage();
        okResp.setReturnCode(ConnAckMessage.BAD_USERNAME_OR_PASSWORD);
        session.writeAndFlush(okResp);
    }


    public void processPubAck(Channel session, PubAckMessage msg) {
        String clientID = NettyUtils.clientID(session);
        int messageID = msg.getMessageID();
        //Remove the message from message store
        ClientSession targetSession = m_sessionsStore.sessionForClient(clientID);
        verifyToActivate(clientID, targetSession);
        targetSession.inFlightAcknowledged(messageID);
    }

    private void verifyToActivate(String clientID, ClientSession targetSession) {
        if (m_clientIDs.containsKey(clientID)) {
            targetSession.activate();
        }
    }

    private static IMessagesStore.StoredMessage asStoredMessage(PublishMessage msg) {
        IMessagesStore.StoredMessage stored = new IMessagesStore.StoredMessage(msg.getPayload().array(), msg.getQos(), msg.getTopicName());
        stored.setRetained(msg.isRetainFlag());
        stored.setMessageID(msg.getMessageID());
        return stored;
    }

    private static IMessagesStore.StoredMessage asStoredMessage(WillMessage will) {
        IMessagesStore.StoredMessage pub = new IMessagesStore.StoredMessage(will.getPayload().array(), will.getQos(), will.getTopic());
        pub.setRetained(will.isRetained());
        return pub;
    }

    public void sendMsg(String clientID, PublishMessage message) {
        ConnectionDescriptor connectionDescriptor = m_clientIDs.get(clientID);
        if (connectionDescriptor != null) {
            if (connectionDescriptor.channel != null) {
                connectionDescriptor.channel.writeAndFlush(message);
            }
        }
    }

    public void processPublish(Channel session, PublishMessage msg) throws Exception {
        LOG.trace("PUB --PUBLISH--> SRV executePublish invoked with {}", msg);
        String clientID = NettyUtils.clientID(session);
        final String topic = msg.getTopicName();
        final QOSType qos = msg.getQos();
        final Integer messageID = msg.getMessageID();
        LOG.info("PUBLISH from clientID <{}> on topic <{}> with QoS {}", clientID, topic, qos);
        String guid = null;
        if (qos == QOSType.MOST_ONE) { //QoS0
            dispatcher4Subscribers(msg);
        } else if (qos == QOSType.LEAST_ONE) { //QoS1
            dispatcher4Subscribers(msg);
            sendPubAck(clientID, messageID);
            LOG.debug("replying with PubAck to MSG ID {}", messageID);
        } else if (qos == QOSType.EXACTLY_ONCE) { //QoS2
//            guid = m_messagesStore.storePublishForFuture(toStoreMsg);
            sendPubRec(clientID, messageID);
            //Next the client will send us a pub rel
            //NB publish to subscribers for QoS 2 happen upon PUBREL from publisher
        }

        if (msg.isRetainFlag()) {
            if (qos == QOSType.MOST_ONE) {
                //QoS == 0 && retain => clean old retained
                m_messagesStore.cleanRetained(topic);
            } else {
                if (!msg.getPayload().hasRemaining()) {
                    m_messagesStore.cleanRetained(topic);
                } else {
                    if (guid == null) {
                        //before wasn't stored
//                        guid = m_messagesStore.storePublishForFuture(toStoreMsg);
                    }
                    m_messagesStore.storeRetained(topic, guid);
                }
            }
        }
        m_interceptor.notifyTopicPublished(msg, clientID);
    }

    /**
     * Intended usage is only for embedded versions of the broker, where the hosting application want to use the
     * broker to send a publish message.
     * Inspired by {@link #processPublish} but with some changes to avoid security check, and the handshake phases
     * for Qos1 and Qos2.
     * It also doesn't notifyTopicPublished because using internally the owner should already know where
     * it's publishing.
     */
    public void internalPublish(PublishMessage msg) {
        final QOSType qos = msg.getQos();
        final String topic = msg.getTopicName();
        LOG.info("embedded PUBLISH on topic <{}> with QoS {}", topic, qos);

        String guid = null;
        IMessagesStore.StoredMessage toStoreMsg = asStoredMessage(msg);
        toStoreMsg.setClientID("BROKER_SELF");
        toStoreMsg.setMessageID(1);
        if (qos == QOSType.EXACTLY_ONCE) { //QoS2
            guid = m_messagesStore.storePublishForFuture(toStoreMsg);
        }
        route2Subscribers(toStoreMsg);

        if (!msg.isRetainFlag()) {
            return;
        }
        if (qos == QOSType.MOST_ONE || !msg.getPayload().hasRemaining()) {
            //QoS == 0 && retain => clean old retained
            m_messagesStore.cleanRetained(topic);
            return;
        }
        if (guid == null) {
            //before wasn't stored
            guid = m_messagesStore.storePublishForFuture(toStoreMsg);
        }
        m_messagesStore.storeRetained(topic, guid);
    }

    /**
     * Specialized version to publish will testament message.
     */
    private void forwardPublishWill(WillMessage will, String clientID) {
        //it has just to publish the message downstream to the subscribers
        //NB it's a will publish, it needs a PacketIdentifier for this conn, default to 1
        Integer messageId = null;
        if (will.getQos() != QOSType.MOST_ONE) {
            messageId = m_sessionsStore.nextPacketID(clientID);
        }

        IMessagesStore.StoredMessage tobeStored = asStoredMessage(will);
        tobeStored.setClientID(clientID);
        tobeStored.setMessageID(messageId);
        route2Subscribers(tobeStored);
    }


    public void dispatcher4Subscribers(PublishMessage message) throws Exception {
        final String topic = message.getTopicName();
        JSONArray uids = imServiceClient.listUidByTopic(topic);
        for (int i = 0; i < uids.size(); i++) {
            String clientID = uids.getString(i);
            ConnectionDescriptor connectionDescriptor = m_clientIDs.get(clientID);
            if (connectionDescriptor != null) {
                Channel channel = connectionDescriptor.channel;
                channel.writeAndFlush(message);
            }

        }
    }

    /**
     * Flood the subscribers with the message to notify. MessageID is optional and should only used for QoS 1 and 2
     */
    void route2Subscribers(IMessagesStore.StoredMessage pubMsg) {
        final QOSType publishingQos = pubMsg.getQos();
    }

    protected void directSend(String clientId, String topic, QOSType qos, ByteBuffer message, boolean retained, Integer messageID) {
        LOG.debug("directSend invoked clientId <{}> on topic <{}> QoS {} retained {} messageID {}", clientId, topic, qos, retained, messageID);
        PublishMessage pubMessage = new PublishMessage();
        pubMessage.setRetainFlag(retained);
        pubMessage.setTopicName(topic);
        pubMessage.setQos(qos);
        pubMessage.setPayload(message);

        //set the PacketIdentifier only for QoS > 0
        if (pubMessage.getQos() != QOSType.MOST_ONE) {
            pubMessage.setMessageID(messageID);
        } else {
            if (messageID != null) {
                throw new RuntimeException("Internal bad error, trying to forwardPublish a QoS 0 message with PacketIdentifier: " + messageID);
            }
        }

        if (m_clientIDs == null) {
            throw new RuntimeException("Internal bad error, found m_clientIDs to null while it should be initialized, somewhere it's overwritten!!");
        }
        LOG.debug("clientIDs are {}", m_clientIDs);
        if (m_clientIDs.get(clientId) == null) {
            //TODO while we were publishing to the target client, that client disconnected,
            // could happen is not an error HANDLE IT
            throw new RuntimeException(String.format("Can't find a ConnectionDescriptor for client <%s> in cache <%s>", clientId, m_clientIDs));
        }
        Channel channel = m_clientIDs.get(clientId).channel;
        LOG.debug("Session for clientId {} is {}", clientId, channel);
        channel.writeAndFlush(pubMessage);
    }

    private void sendPubRec(String clientID, int messageID) {
        LOG.trace("PUB <--PUBREC-- SRV sendPubRec invoked for clientID {} with messageID {}", clientID, messageID);
        PubRecMessage pubRecMessage = new PubRecMessage();
        pubRecMessage.setMessageID(messageID);
        m_clientIDs.get(clientID).channel.writeAndFlush(pubRecMessage);
    }

    private void sendPubAck(String clientId, int messageID) {
        LOG.trace("sendPubAck invoked");
        PubAckMessage pubAckMessage = new PubAckMessage();
        pubAckMessage.setMessageID(messageID);

        try {
            if (m_clientIDs == null) {
                throw new RuntimeException("Internal bad error, found m_clientIDs to null while it should be initialized, somewhere it's overwritten!!");
            }
            LOG.debug("clientIDs are {}", m_clientIDs);
            if (m_clientIDs.get(clientId) == null) {
                throw new RuntimeException(String.format("Can't find a ConnectionDescriptor for client %s in cache %s", clientId, m_clientIDs));
            }
            m_clientIDs.get(clientId).channel.writeAndFlush(pubAckMessage);
        } catch (Throwable t) {
            LOG.error(null, t);
        }
    }

    /**
     * Second phase of a publish QoS2 protocol, sent by publisher to the broker. Search the stored message and publish
     * to all interested subscribers.
     */
    public void processPubRel(Channel channel, PubRelMessage msg) {
        String clientID = NettyUtils.clientID(channel);
        int messageID = msg.getMessageID();
        LOG.debug("PUB --PUBREL--> SRV processPubRel invoked for clientID {} ad messageID {}", clientID, messageID);
        ClientSession targetSession = m_sessionsStore.sessionForClient(clientID);
        verifyToActivate(clientID, targetSession);
        IMessagesStore.StoredMessage evt = targetSession.storedMessage(messageID);
        route2Subscribers(evt);

        if (evt.isRetained()) {
            final String topic = evt.getTopic();
            if (!evt.getMessage().hasRemaining()) {
                m_messagesStore.cleanRetained(topic);
            } else {
                m_messagesStore.storeRetained(topic, evt.getGuid());
            }
        }

        sendPubComp(clientID, messageID);
    }

    private void sendPubComp(String clientID, int messageID) {
        LOG.debug("PUB <--PUBCOMP-- SRV sendPubComp invoked for clientID {} ad messageID {}", clientID, messageID);
        PubCompMessage pubCompMessage = new PubCompMessage();
        pubCompMessage.setMessageID(messageID);

        m_clientIDs.get(clientID).channel.writeAndFlush(pubCompMessage);
    }

    public void processPubRec(Channel channel, PubRecMessage msg) {
        String clientID = NettyUtils.clientID(channel);
        int messageID = msg.getMessageID();
        ClientSession targetSession = m_sessionsStore.sessionForClient(clientID);
        verifyToActivate(clientID, targetSession);
        //remove from the inflight and move to the QoS2 second phase queue
        targetSession.inFlightAcknowledged(messageID);
        targetSession.secondPhaseAckWaiting(messageID);
        //once received a PUBREC reply with a PUBREL(messageID)
        LOG.debug("\t\tSRV <--PUBREC-- SUB processPubRec invoked for clientID {} ad messageID {}", clientID, messageID);
        PubRelMessage pubRelMessage = new PubRelMessage();
        pubRelMessage.setMessageID(messageID);
        pubRelMessage.setQos(QOSType.LEAST_ONE);

        channel.writeAndFlush(pubRelMessage);
    }

    public void processPubComp(Channel channel, PubCompMessage msg) {
        String clientID = NettyUtils.clientID(channel);
        int messageID = msg.getMessageID();
        LOG.debug("\t\tSRV <--PUBCOMP-- SUB processPubComp invoked for clientID {} ad messageID {}", clientID, messageID);
        //once received the PUBCOMP then remove the message from the temp memory
        ClientSession targetSession = m_sessionsStore.sessionForClient(clientID);
        verifyToActivate(clientID, targetSession);
        targetSession.secondPhaseAcknowledged(messageID);
    }

    public void processDisconnect(Channel channel) throws InterruptedException {
        String clientID = NettyUtils.clientID(channel);
        boolean cleanSession = NettyUtils.cleanSession(channel);
        LOG.info("DISCONNECT client <{}> with clean session {}", clientID, cleanSession);
        ClientSession clientSession = m_sessionsStore.sessionForClient(clientID);
        clientSession.disconnect();

        m_clientIDs.remove(clientID);
        channel.close();

        //cleanup the will store
        m_willStore.remove(clientID);

        m_interceptor.notifyClientDisconnected(clientID);
        LOG.info("DISCONNECT client <{}> finished", clientID, cleanSession);
    }

    public void processConnectionLost(String clientID, boolean sessionStolen, Channel channel) {
        ConnectionDescriptor oldConnDescr = new ConnectionDescriptor(clientID, channel, true);
        m_clientIDs.remove(clientID, oldConnDescr);

        //If already removed a disconnect message was already processed for this clientID
        if (sessionStolen) {
            //de-activate the subscriptions for this ClientID
            ClientSession clientSession = m_sessionsStore.sessionForClient(clientID);
            clientSession.deactivate();
            LOG.info("Lost connection with client <{}>", clientID);
        }
        //publish the Will message (if any) for the clientID
        if (!sessionStolen && m_willStore.containsKey(clientID)) {
            WillMessage will = m_willStore.get(clientID);
            forwardPublishWill(will, clientID);
            m_willStore.remove(clientID);
        }
    }

    /**
     * Remove the clientID from topic subscription, if not previously subscribed,
     * doesn't reply any error
     */
    public void processUnsubscribe(Channel channel, UnsubscribeMessage msg) throws Exception {
        List<String> topics = msg.topicFilters();
        int messageID = msg.getMessageID();
        String clientID = NettyUtils.clientID(channel);
        LOG.debug("UNSUBSCRIBE subscription on topics {} for clientID <{}>", topics, clientID);
        for (String topic : topics) {
            imServiceClient.unSubscribeTopic(clientID, topic);
            m_interceptor.notifyTopicUnsubscribed(topic, clientID);
        }
        //ack the client
        UnsubAckMessage ackMessage = new UnsubAckMessage();
        ackMessage.setMessageID(messageID);

        LOG.info("replying with UnsubAck to MSG ID {}", messageID);
        channel.writeAndFlush(ackMessage);
    }

    public void processSubscribe(Channel channel, SubscribeMessage msg) throws Exception {

        String clientID = NettyUtils.clientID(channel);
        LOG.debug("SUBSCRIBE client <{}> packetID {}", clientID, msg.getMessageID());

        //ack the client
        SubAckMessage ackMessage = new SubAckMessage();
        ackMessage.setMessageID(msg.getMessageID());

        List<Subscription> newSubscriptions = new ArrayList<>();
        for (SubscribeMessage.Couple req : msg.subscriptions()) {
            QOSType qos = QOSType.valueOf(req.qos);
            Subscription newSubscription = new Subscription(clientID, req.topicFilter, qos);
            boolean valid = imServiceClient.subscribeTopic(clientID, req.topicFilter);
            ackMessage.addType(valid ? qos : QOSType.FAILURE);
            if (valid) {
                newSubscriptions.add(newSubscription);
            }
        }
        channel.writeAndFlush(ackMessage);
        //fire the publish
        for (Subscription subscription : newSubscriptions) {
            subscribeSingleTopic(subscription);
        }
    }

    private boolean subscribeSingleTopic(final Subscription newSubscription) throws Exception {
        return imServiceClient.subscribeTopic(newSubscription.getClientId(), newSubscription.getTopicFilter());
    }
}
