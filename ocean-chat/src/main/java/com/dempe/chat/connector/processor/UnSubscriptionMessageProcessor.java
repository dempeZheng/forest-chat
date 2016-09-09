package com.dempe.chat.connector.processor;

import com.dempe.chat.common.mqtt.messages.UnsubAckMessage;
import com.dempe.chat.common.mqtt.messages.UnsubscribeMessage;
import com.dempe.chat.connector.NettyUtils;
import com.dempe.chat.connector.store.ClientSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/11
 * Time: 20:40
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UnSubscriptionMessageProcessor extends MessageProcessor {

    public void processUnsubscribe(Channel channel, UnsubscribeMessage msg) {
        List<String> topics = msg.topicFilters();
        int messageID = msg.getMessageID();
        String clientID = NettyUtils.clientID(channel);

        LOGGER.debug("UNSUBSCRIBE subscription on topics {} for clientID <{}>", topics, clientID);

        ClientSession clientSession = m_sessionsStore.sessionForClient(clientID);
        for (String topic : topics) {
        }

        //ack the client
        UnsubAckMessage ackMessage = new UnsubAckMessage();
        ackMessage.setMessageID(messageID);

        LOGGER.info("replying with UnsubAck to MSG ID {}", messageID);
        channel.writeAndFlush(ackMessage);
    }

}
