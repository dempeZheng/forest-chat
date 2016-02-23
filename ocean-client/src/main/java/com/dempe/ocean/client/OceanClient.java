package com.dempe.ocean.client;

import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class OceanClient implements Client {
    private MQTTClient client;

    public OceanClient(NodeDetails nodeDetails) throws Exception {
        client = new MQTTClient(nodeDetails);
    }

    @Override
    public Response subscribe(Long uid, Long topSid, Long subSid) {
        return null;
    }

    @Override
    public void unSubscribe(Long uid, Long topSid, Long subSid) {

    }

    @Override
    public Response publish(Long uid, Long topSid, Long subSid, Request request) {
        client.send(topSid + "|" + subSid, request);
        return null;
    }

    @Override
    public Response publishBC(Long topSid, Long subSid, Request request) {
        return null;
    }

    @Override
    public Response publishMultiBC(List<Long> uidList, Long topSid, Long subSid, Request request) {
        return null;
    }
}
