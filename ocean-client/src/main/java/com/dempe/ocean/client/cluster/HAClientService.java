package com.dempe.ocean.client.cluster;


import com.dempe.ocean.client.Client;
import com.dempe.ocean.client.ForestClient;
import com.dempe.ocean.client.LiveClient;
import com.dempe.ocean.common.protocol.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class HAClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HAClientService.class);

    private static HAForestClient haForestClient;

    public HAClientService(String name) throws Exception {
        if (haForestClient == null) {
            synchronized (HAClientService.class) {
                if (haForestClient == null) {
                    haForestClient = new HAForestClient(name);
                }
            }
        }
    }


    public void sendOnly(Request request) throws Exception {
        Client client = haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return;
        }
        client.sendOnly(request);

    }

    public void sendBuffer(ByteBuffer buffer){
        Client client = haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", buffer);
            return;
        }
        LOGGER.info("sendBuffer:{}",buffer);
        client.sendBuffer(buffer);
    }


}
