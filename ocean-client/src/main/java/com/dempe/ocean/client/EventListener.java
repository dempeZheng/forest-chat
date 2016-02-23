package com.dempe.ocean.client;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 15:03
 * To change this template use File | Settings | File Templates.
 */
public class EventListener implements Listener {

    public void onDisconnected() {
    }

    public void onConnected() {
    }

    public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
        // You can now process a received message from a topic.
        // Once process execute the ack runnable.
        ack.run();
    }

    public void onFailure(Throwable value) {
    }
}
