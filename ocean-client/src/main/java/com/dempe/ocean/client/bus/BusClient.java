package com.dempe.ocean.client.bus;

import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.protocol.BusMessage;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class BusClient implements Client {

    private MQTTClient client;

    public BusClient(NodeDetails nodeDetails) throws Exception {
        client = new MQTTClient(nodeDetails);
    }


    public void connect(String uid, String pwd) throws Exception {
        client.connect(uid, pwd);
    }

    @Override
    public void subscribe(String topic) {

        client.subscribe(topic);
    }


    @Override
    public void unSubscribe(String topic) {
        client.unSubscribe(topic);
    }

    @Override
    public void publish(String topic, BusMessage message) {
        client.publish(topic, message);
    }

    @Override
    public void publish(String topic, byte[] bytes) {
        client.publish(topic,bytes);
    }


    @Override
    public Future<Message> receive() {
        return client.receive();
    }

    public Message publishBC(String topic, byte[] bytes) {
        client.publishBC(topic, bytes);
        return null;
    }
}
