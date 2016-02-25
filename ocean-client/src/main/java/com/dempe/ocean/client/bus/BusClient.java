package com.dempe.ocean.client.bus;

import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
import org.fusesource.mqtt.client.Callback;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class BusClient implements LiveClient {

    private MQTTClient client;

    public BusClient(NodeDetails nodeDetails) throws Exception {
        client = new MQTTClient(nodeDetails);
    }


    @Override
    public void subscribe(Long uid, String topic) {

        client.subscribe(uid, topic, new Callback<byte[]>() {
            @Override
            public void onSuccess(byte[] value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }

    @Override
    public void unSubscribe(Long uid, String topic) {

        client.unSubscribe(uid, topic, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }

    @Override
    public Response publish(Long uid, String topic, Request request) {
        return client.publish(uid, topic, request, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }

    @Override
    public Response publishBC(String topic, Request request) {
        return client.publishBC(topic, request, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }

    @Override
    public Response publishMultiBC(List<Long> uidList, String topic, Request request) {
        return null;
    }
}
