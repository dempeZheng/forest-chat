package com.dempe.ocean.client.bus.cluster;

import com.dempe.ocean.client.bus.LiveClient;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
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

    public HABusCliService(String name) throws Exception {
        if (haBusClient == null) {
            synchronized (HABusCliService.class) {
                if (haBusClient == null) {
                    haBusClient = new HABusClient(name);
                }
            }
        }
    }


    @Override
    public void subscribe(Long uid, String topic) {
        haBusClient.getClient().subscribe(uid, topic);
    }

    @Override
    public void unSubscribe(Long uid, String topic) {

        haBusClient.getClient().unSubscribe(uid, topic);
    }

    @Override
    public Response publish(Long uid, String topic, Request request) {
        return haBusClient.getClient().publish(uid, topic, request);
    }

    @Override
    public Response publishBC(String topic, Request request) {
        return haBusClient.getClient().publishBC(topic, request);
    }

    @Override
    public Response publishMultiBC(List<Long> uidList, String topic, Request request) {
        return haBusClient.getClient().publishMultiBC(uidList, topic, request);
    }
}
