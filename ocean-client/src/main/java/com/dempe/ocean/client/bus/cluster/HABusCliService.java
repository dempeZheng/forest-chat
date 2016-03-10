package com.dempe.ocean.client.bus.cluster;

import com.dempe.ocean.client.NoAvailableClientException;
import com.dempe.ocean.client.bus.Client;
import com.dempe.ocean.common.protocol.BusMessage;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/25
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class HABusCliService implements Client {

    private final static Logger LOGGER = LoggerFactory.getLogger(HABusCliService.class);

    private static HABusClient haBusClient;

    private Client client;

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
       checkClient();
        client.connect(uid, pwd);
    }

    @Override
    public void subscribe(String topic) throws NoAvailableClientException {
        checkClient();
        client.subscribe(topic);
    }


    @Override
    public void unSubscribe(String topic) throws NoAvailableClientException {
        checkClient();
        client.unSubscribe(topic);
    }

    @Override
    public void publish(String topic, BusMessage message) throws NoAvailableClientException {
        checkClient();
        client.publish(topic, message);
    }

    @Override
    public void publish(String topic, byte[] bytes) throws NoAvailableClientException {
        checkClient();
        client.publish(topic, bytes);
    }


    @Override
    public Future<Message> receive() throws NoAvailableClientException {
        checkClient();
        return client.receive();
    }

    private void checkClient() throws NoAvailableClientException {
        if(client==null){
            client = haBusClient.getClient();
        }
        if(client==null){
            throw  new NoAvailableClientException();
        }
    }
}
