package com.dempe.ocean.client.node;


import com.dempe.ocean.common.protocol.Response;
import com.dempe.ocean.common.protocol.mqtt.AbstractMessage;
import com.dempe.ocean.common.protocol.mqtt.PublishMessage;
import com.dempe.ocean.core.ProtocolProcessor;
import com.dempe.ocean.core.ProtocolProcessorNew;
import com.dempe.ocean.core.spi.persistence.UidSessionStore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * client的消息处理handler,处理服务端返回的消息
 * User: Dempe
 * Date: 2016/1/28
 * Time: 18:47
 * To change this template use File | Settings | File Templates.
 */
public class NodeClientHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeClientHandler.class);

    private ProtocolProcessorNew protocolProcessor;

    public NodeClientHandler(ProtocolProcessorNew protocolProcessor) {
        this.protocolProcessor = protocolProcessor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response resp = (Response) msg;
        String uid = resp.getUid();
        uid = "12345";

        LOGGER.info("resp:{}", resp);

        Channel session = getSessionByUid(uid);
        if (session == null) {
            return;
        }
        PublishMessage message = new PublishMessage();
        message.setTopicName("foo");
        message.setQos(AbstractMessage.QOSType.UNICAST);

        byte[] bytes = resp.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        message.setPayload(buffer);
        message.setMessageID(1);


        protocolProcessor.processPublish(session, message);

    }

    public Channel getSessionByUid(String uid) {
        return UidSessionStore.getSessionByUid(uid);
    }

}

