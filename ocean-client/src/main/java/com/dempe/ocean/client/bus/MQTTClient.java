package com.dempe.ocean.client.bus;


import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Response;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class MQTTClient {


    private FutureConnection connection;

    private MQTT mqtt;

    private final static Logger LOGGER = LoggerFactory.getLogger(MQTTClient.class);


    public MQTTClient() throws Exception {
        init("localhost", 1833);
    }

    public MQTTClient(String host, int port) throws Exception {
        init(host, port);
    }

    public MQTTClient(NodeDetails nodeDetails) throws Exception {
        this(nodeDetails.getIp(), nodeDetails.getPort());
    }

    public void init(String host, int port) throws Exception {
        mqtt = new MQTT();
        mqtt.setHost(host, port);
    }

    public void connect(String uid, String pwd) throws Exception {
        mqtt.setUserName(uid);
        mqtt.setPassword(pwd);
        mqtt.setClientId(uid);
        connection = mqtt.futureConnection();
        connection.connect();

    }


    public void subscribe(String topic) {
        connection.subscribe(new Topic[]{new Topic(topic, QoS.AT_LEAST_ONCE)});
    }


    public void unSubscribe(String topic) {
        connection.unsubscribe(new UTF8Buffer[]{new UTF8Buffer(topic)});
    }


    public Response publish(String topic, BusMessage request) {
        connection.publish(topic, request.toByteArray(), QoS.AT_LEAST_ONCE, false);
        return null;
    }


    public Response publishBC(String topic, BusMessage request) {
        connection.publish(topic, request.toByteArray(), QoS.AT_LEAST_ONCE, false);
        return null;
    }

    public Future<Message> receive() {
        return connection.receive();
    }


}
