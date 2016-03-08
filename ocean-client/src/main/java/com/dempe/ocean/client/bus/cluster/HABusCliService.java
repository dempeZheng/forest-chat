package com.dempe.ocean.client.bus.cluster;

import com.dempe.ocean.client.bus.LiveClient;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Response;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/25
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class HABusCliService implements LiveClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(HABusCliService.class);

    private static HABusClient haBusClient;

    private LiveClient client;

    public HABusCliService(String name) throws Exception {
        if (haBusClient == null) {
            synchronized (HABusCliService.class) {
                if (haBusClient == null) {
                    haBusClient = new HABusClient(name);
                    client = haBusClient.getClient();

                }
            }
        }
    }

    @Override
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
    public Response publish(String topic, BusMessage request) {
        return client.publish(topic, request);
    }

    @Override
    public Response publish(String topic, byte[] bytes) {
        return client.publish(topic, bytes);
    }


    @Override
    public Response publishBC(String topic, BusMessage request) {
        return client.publishBC(topic, request);
    }

    @Override
    public Response publishBC(String topic, byte[] bytes) {
        return client.publishBC(topic, bytes);
    }

    @Override
    public Response publishMultiBC(List<Long> uidList, String topic, BusMessage request) {
        return client.publishMultiBC(uidList, topic, request);
    }

    @Override
    public Future<Message> receive() {
        return client.receive();
    }
}
