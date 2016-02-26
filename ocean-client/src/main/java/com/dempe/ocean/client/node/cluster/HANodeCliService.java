package com.dempe.ocean.client.node.cluster;


import com.dempe.ocean.client.NoAvailableClientException;
import com.dempe.ocean.client.node.Client;
import com.dempe.ocean.common.protocol.Request;
import io.netty.buffer.ByteBuf;
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
public class HANodeCliService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HANodeCliService.class);

    private static HANodeClient haForestClient;

    public HANodeCliService(String name) throws Exception {
        if (haForestClient == null) {
            synchronized (HANodeCliService.class) {
                if (haForestClient == null) {
                    haForestClient = new HANodeClient(name);
                }
            }
        }
    }


    public void sendOnly(Request request) throws Exception {
        Client client = haForestClient.getClient();
        if (client == null) {
            throw new NoAvailableClientException();
        }
        client.sendOnly(request);

    }

    public void sendBuffer(ByteBuf buffer) {
        Client client = haForestClient.getClient();
        if (client == null) {
            throw new NoAvailableClientException();
        }
        LOGGER.info("sendBuffer:{}", buffer);
        client.sendBuffer(buffer);
    }

    public void sendBytes(byte[] bytes) {
        Client client = haForestClient.getClient();
        if (client == null) {
            throw new NoAvailableClientException();
        }
        client.sendBytes(bytes);
    }


}
