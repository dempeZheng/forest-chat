package com.dempe.chat.connector;

import com.dempe.chat.common.Utils;
import com.dempe.chat.common.mqtt.messages.*;
import com.dempe.chat.connector.processor.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dempe.chat.common.mqtt.messages.AbstractMessage.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/11
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
@ChannelHandler.Sharable
@Component
public class MQTTHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTHandler.class);

    @Autowired
    private ConnMessageProcessor connMessageProcessor;
    @Autowired
    private PublishMessageProcessor publishMessageProcessor;
    @Autowired
    private SubscribeMessageProcessor subscribeMessageProcessor;
    @Autowired
    private UnSubscriptionMessageProcessor unSubscriptionMessageProcessor;
    @Autowired
    private DisconnectMessageProcessor disconnectMessageProcessor;


    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object message) {
        AbstractMessage msg = (AbstractMessage) message;
        LOGGER.info("Received a message of type {}", Utils.msgType2String(msg.getMessageType()));
        Channel channel = ctx.channel();
        try {
            switch (msg.getMessageType()) {
                case CONNECT:
                    // 处理connect msg
                    LOGGER.debug("---------handle connect message-------------");
                    connMessageProcessor.processConnect(channel, (ConnectMessage) msg);
                    break;
                case SUBSCRIBE:
                    LOGGER.debug("---------handle subscribe message-------------");
                    subscribeMessageProcessor.processSubscribe(channel, (SubscribeMessage) msg);
                    break;
                case UNSUBSCRIBE:
                    LOGGER.debug("---------handle unsubscribe message-------------");
                    unSubscriptionMessageProcessor.processUnsubscribe(channel, (UnsubscribeMessage) msg);
                    break;
                case PUBLISH:
                    LOGGER.debug("---------handle publish message-------------");
                    publishMessageProcessor.processPublish(channel, (PublishMessage) msg);
                    break;
                case PUBREC:
                    break;
                case PUBCOMP:
                    break;
                case PUBREL:
                    break;
                case PUBACK:
                    break;
                case DISCONNECT:
                    LOGGER.debug("---------handle disconnect message-------------");
                    disconnectMessageProcessor.processDisconnect(channel);
                    break;
                case PINGREQ:
                    handlePingMsg(ctx);
                    break;
            }
        } catch (Exception ex) {
            LOGGER.error("Bad error in processing the message", ex);
        }
    }

    private void handlePingMsg(final ChannelHandlerContext ctx) {
        PingRespMessage pingResp = new PingRespMessage();
        ctx.writeAndFlush(pingResp);
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
            new SubscribeMessageProcessor().processConnectionLost(clientID, stolen, ctx.channel());
        }
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof CorruptedFrameException) {
            //something goes bad with decoding
            LOGGER.warn("Error decoding a packet, probably a bad formatted packet, message: " + cause.getMessage());
        } else {
            LOGGER.error("Ugly error on networking", cause);
        }
        ctx.close();
    }
}
