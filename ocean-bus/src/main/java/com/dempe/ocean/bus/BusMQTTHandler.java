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

import com.dempe.ocean.client.Callback;
import com.dempe.ocean.client.ha.DefaultClientService;
import com.dempe.ocean.common.MsgType;
import com.dempe.ocean.common.R;
import com.dempe.ocean.common.pack.Unpack;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
import com.dempe.ocean.common.protocol.mqtt.*;
import com.dempe.ocean.core.NettyUtils;
import com.dempe.ocean.core.ProtocolProcessor;
import com.dempe.ocean.core.spi.persistence.UidSessionStore;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

import static com.dempe.ocean.common.protocol.mqtt.AbstractMessage.*;

/**
 * @author andrea
 */
@Sharable
public class BusMQTTHandler extends ChannelHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(BusMQTTHandler.class);
    private final ProtocolProcessor m_processor;

    public BusMQTTHandler(ProtocolProcessor processor) {
        m_processor = processor;
    }

    private final static Map<String, DefaultClientService> nameClientMap = Maps.newConcurrentMap();


    /**
     * 根据节点名称获取对应的HAClientService
     * HAClientService 会选择路由策略选择合适的业务进程，将消息透传
     *
     * @param name
     * @return
     * @throws Exception
     */
    private DefaultClientService getClientServiceByName(String name) throws Exception {
        DefaultClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new DefaultClientService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }


    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object message) {
        AbstractMessage msg = (AbstractMessage) message;
        LOG.info("Received a message of type {}", Utils.msgType2String(msg.getMessageType()));
        try {
            switch (msg.getMessageType()) {
                case CONNECT:
                    m_processor.processConnect(ctx.channel(), (ConnectMessage) msg);
                    break;
                case SUBSCRIBE:
                    m_processor.processSubscribe(ctx.channel(), (SubscribeMessage) msg);
                    break;
                case UNSUBSCRIBE:
                    m_processor.processUnsubscribe(ctx.channel(), (UnsubscribeMessage) msg);
                    break;
                case PUBLISH:
                    processPublish(ctx, msg);
                    break;
                case PUBREC:
                    m_processor.processPubRec(ctx.channel(), (PubRecMessage) msg);
                    break;
                case PUBCOMP:
                    m_processor.processPubComp(ctx.channel(), (PubCompMessage) msg);
                    break;
                case PUBREL:
                    m_processor.processPubRel(ctx.channel(), (PubRelMessage) msg);
                    break;
                case DISCONNECT:
                    m_processor.processDisconnect(ctx.channel());
                    break;
                case PUBACK:
                    m_processor.processPubAck(ctx.channel(), (PubAckMessage) msg);
                    break;
                case PINGREQ:
                    PingRespMessage pingResp = new PingRespMessage();
                    ctx.writeAndFlush(pingResp);
                    break;
            }
        } catch (Exception ex) {
            LOG.error("Bad error in processing the message", ex);
        }
    }

    private void processPublish(final ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        final PublishMessage publishMessage = (PublishMessage) msg;
        String topic = publishMessage.getTopicName();
        Integer messageID = publishMessage.getMessageID();
        ByteBuffer payload = publishMessage.getPayload();
        payload = payload.order(ByteOrder.LITTLE_ENDIAN);
        BusMessage busMessage = new BusMessage().unmarshal(new Unpack(payload));
        short msgType = busMessage.getMsgType();
        String daemonName = busMessage.getDaemonName();
        // 如果消息类型为单播，且透传的进程非bus进程，将消息传递给下一个handler(分发消息到相应的业务进程)
        if (StringUtils.equals("bus",topic) && msgType == MsgType.UNICAST.getValue() && daemonName != R.FOREST_BUS_NAME) {
            DefaultClientService clientService = getClientServiceByName(daemonName);
            Request request = busMessage.getRequest();
            request.setMessageID(messageID);
            clientService.send(request, new Callback() {
                @Override
                public void onReceive(Object message) {
                    Response resp = (Response) message;
                    byte[] bytes = resp.toByteArray();
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    publishMessage.setPayload(buffer);
                    ctx.executor().execute(new Runnable() {
                        @Override
                        public void run() {
                            ctx.writeAndFlush(publishMessage);
                        }
                    });
                }
            });

            // 单播消息，如果topic非空，则定义为点对点的聊天，单播消息的topic为聊天对象的uid
        } else if (StringUtils.isNotBlank(topic) && msgType == MsgType.UNICAST.getValue()) {
            // 获取channel，将消息直接发送到对应的client
            // 单播消息 topic非空情况下，定义存储为uid
            String uid = topic;
            Channel session = UidSessionStore.getSessionByUid(uid);
            if (session != null) {
                ByteBuffer wrap = ByteBuffer.wrap(busMessage.getRequest().toByteArray());
                publishMessage.setPayload(wrap);
                session.writeAndFlush(publishMessage);
            }
            // 如果为广播
        } else if (msgType == MsgType.BCSUBCH.getValue()) {
            // 覆盖publishMessage 中的payload，去掉对客户端无价值的外层协议(daemonName&msgType)
            ByteBuffer wrap = ByteBuffer.wrap(busMessage.getRequest().toByteArray());
            publishMessage.setPayload(wrap);
            m_processor.processPublish(ctx.channel(), publishMessage);

        } else if (msgType == MsgType.AREA_MULTICAST.getValue()) {
            // 多播 逻辑待实现
        }

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String clientID = NettyUtils.clientID(ctx.channel());
        if (clientID != null && !clientID.isEmpty()) {
            //if the channel was of a correctly connected client, inform messaging
            //else it was of a not completed CONNECT message or sessionStolen
            boolean stolen = false;
            Boolean stolenAttr = NettyUtils.sessionStolen(ctx.channel());
            if (stolenAttr != null && stolenAttr == Boolean.TRUE) {
                stolen = true;
            }
            m_processor.processConnectionLost(clientID, stolen, ctx.channel());
        }
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof CorruptedFrameException) {
            //something goes bad with decoding
            LOG.warn("Error decoding a packet, probably a bad formatted packet, message: " + cause.getMessage());
        } else {
            UidSessionStore.remove(NettyUtils.userName(ctx.channel()));
            LOG.error("Ugly error on networking");
        }
        ctx.close();
    }
}
