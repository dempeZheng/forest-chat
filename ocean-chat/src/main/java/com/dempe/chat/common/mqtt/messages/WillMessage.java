package com.dempe.chat.common.mqtt.messages;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/11
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class WillMessage {
    private final String topic;
    private final ByteBuffer payload;
    private final boolean retained;
    private final AbstractMessage.QOSType qos;

    public WillMessage(String topic, ByteBuffer payload, boolean retained, AbstractMessage.QOSType qos) {
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

    public AbstractMessage.QOSType getQos() {
        return qos;
    }
}
