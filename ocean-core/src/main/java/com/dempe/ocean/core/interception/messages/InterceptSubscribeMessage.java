package com.dempe.ocean.core.interception.messages;

import com.dempe.ocean.common.protocol.mqtt.AbstractMessage;
import com.dempe.ocean.core.spi.impl.subscriptions.Subscription;

/**
 * @author Wagner Macedo
 */
public class InterceptSubscribeMessage {
    private final Subscription subscription;

    public InterceptSubscribeMessage(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getClientID() {
        return subscription.getClientId();
    }

    public AbstractMessage.QOSType getRequestedQos() {
        return subscription.getRequestedQos();
    }

    public String getTopicFilter() {
        return subscription.getTopicFilter();
    }
}
