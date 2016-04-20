package com.dempe.chat.connector.processor;

import com.dempe.chat.common.mqtt.messages.AbstractMessage;
import com.dempe.chat.common.mqtt.messages.PublishMessage;
import com.dempe.chat.common.mqtt.messages.WillMessage;
import com.dempe.chat.connector.ConnectionDescriptor;
import com.dempe.chat.connector.store.ClientSession;
import com.dempe.chat.connector.store.SessionStoreImpl;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/11
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class MessageProcessor {

    protected final static Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

    protected static ConcurrentMap<String, ConnectionDescriptor> m_clientIDs = Maps.newConcurrentMap();

    protected static ConcurrentMap<String, WillMessage> m_willStore = Maps.newConcurrentMap();
    protected static SessionStoreImpl m_sessionsStore = new SessionStoreImpl();


    protected void setIdleTime(ChannelPipeline pipeline, int idleTime) {
        if (pipeline.names().contains("idleStateHandler")) {
            pipeline.remove("idleStateHandler");
        }
        pipeline.addFirst("idleStateHandler", new IdleStateHandler(0, 0, idleTime));
    }

    protected void directSend(ClientSession clientsession, String topic, AbstractMessage.QOSType qos, ByteBuffer message, boolean retained, Integer messageID) {
        String clientId = clientsession.getClientID();
        LOGGER.debug("directSend invoked clientId <{}> on topic <{}> QoS {} retained {} messageID {}", clientId, topic, qos, retained, messageID);
        PublishMessage pubMessage = new PublishMessage();
        pubMessage.setRetainFlag(retained);
        pubMessage.setTopicName(topic);
        pubMessage.setQos(qos);
        pubMessage.setPayload(message);
        LOGGER.info("send publish message to <{}> on topic <{}>", clientId, topic);
        //set the PacketIdentifier only for QoS > 0
        if (pubMessage.getQos() != AbstractMessage.QOSType.MOST_ONE) {
            pubMessage.setMessageID(messageID);
        } else {
            if (messageID != null) {
                throw new RuntimeException("Internal bad error, trying to forwardPublish a QoS 0 message with PacketIdentifier: " + messageID);
            }
        }
        if (m_clientIDs == null) {
            throw new RuntimeException("Internal bad error, found m_clientIDs to null while it should be initialized, somewhere it's overwritten!!");
        }
        LOGGER.debug("clientIDs are {}", m_clientIDs);
        if (m_clientIDs.get(clientId) == null) {
            //TODO while we were publishing to the target client, that client disconnected,
            // could happen is not an error HANDLE IT
            throw new RuntimeException(String.format("Can't find a ConnectionDescriptor for client <%s> in cache <%s>", clientId, m_clientIDs));
        }
        Channel channel = m_clientIDs.get(clientId).channel;
        LOGGER.debug("Session for clientId {} is {}", clientId, channel);
        channel.writeAndFlush(pubMessage);
    }

    public void processConnectionLost(String clientID, boolean sessionStolen, Channel channel) {
        ConnectionDescriptor oldConnDescr = new ConnectionDescriptor(clientID, channel, true);
        m_clientIDs.remove(clientID, oldConnDescr);
        //If already removed a disconnect message was already processed for this clientID
        if (sessionStolen) {
            //de-activate the subscriptions for this ClientID
            ClientSession clientSession = m_sessionsStore.sessionForClient(clientID);
            clientSession.deactivate();
            LOGGER.info("Lost connection with client <{}>", clientID);
        }
        //publish the Will message (if any) for the clientID
        if (!sessionStolen && m_willStore.containsKey(clientID)) {
            WillMessage will = m_willStore.get(clientID);
            forwardPublishWill(will, clientID);
            m_willStore.remove(clientID);
        }
    }


    /**
     * TODO
     * Specialized version to publish will testament message.
     */
    private void forwardPublishWill(WillMessage will, String clientID) {
        //it has just to publish the message downstream to the subscribers
        //NB it's a will publish, it needs a PacketIdentifier for this conn, default to 1
        short messageId = 0;
        if (will.getQos() != AbstractMessage.QOSType.MOST_ONE) {
            messageId = m_sessionsStore.sessionForClient(clientID).getNextMessageId();
        }

//        IMessagesStore.StoredMessage tobeStored = asStoredMessage(will);
//        tobeStored.setClientID(clientID);
//        tobeStored.setMessageID(messageId);
//        route2Subscribers(tobeStored);
    }


}
