package com.dempe.chat.connector.processor;

import com.dempe.chat.common.TopicType;
import com.dempe.chat.common.mqtt.messages.AbstractMessage;
import com.dempe.chat.common.mqtt.messages.PublishMessage;
import com.dempe.chat.connector.NettyUtils;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

/**
 * 扩展publish消息，规定topicName为空的时候为单播请求，即问答模式
 * publish消息里层协议封装
 * 1.有新的消息后server主动push
 * 2.client收到push消息后，发sync同步消息，
 * 3.server按redis sort set里面版本好发送消息给客户端
 * 4.client返回收到的序列号
 * User: Dempe
 * Date: 2016/4/11
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public class PublishMessageProcessor extends MessageProcessor {

    /**
     * 1.存储消息到mongodb
     * 2.
     *
     * @param session
     * @param msg
     */
    public void processPublish(Channel session, PublishMessage msg) {
        LOGGER.trace("PUB --PUBLISH--> SRV executePublish invoked with {}", msg);
        String clientID = NettyUtils.clientID(session);
        final String topic = msg.getTopicName();
        //check if the topic can be wrote
        final AbstractMessage.QOSType qos = msg.getQos();
        final Integer messageID = msg.getMessageID();
        LOGGER.info("PUBLISH from clientID <{}> on topic <{}> with QoS {}", clientID, topic, qos);

        if (StringUtils.isBlank(topic)) {
            // 规定如果是null topic，则为内置协议

        } else if (StringUtils.startsWith(topic, TopicType.FRIEND.getType())) {
            // 发给朋友的消息



        } else if (StringUtils.startsWith(topic, TopicType.GROUP.getType())) {
            // 发给群组的消息

        } else if (StringUtils.startsWith(topic, TopicType.MYSELF.getType())) {
            // 发给自己的，属于传统的问答模式的消息，

        }


    }

}
