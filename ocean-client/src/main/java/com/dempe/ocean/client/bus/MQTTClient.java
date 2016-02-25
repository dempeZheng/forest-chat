package com.dempe.ocean.client.bus;


import com.dempe.ocean.client.bus.mqtt.CallbackConnection;
import com.dempe.ocean.client.bus.mqtt.MQTTCli;
import com.dempe.ocean.client.bus.mqtt.QoS;
import com.dempe.ocean.client.bus.mqtt.Topic;
import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;


/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class MQTTClient {


    private CallbackConnection connection;

    private MQTTCli mqtt;

    public MQTTClient() throws Exception {
        init("localhost", 1833, null, null);
    }

    public MQTTClient(String host, int port) throws Exception {
        init(host, port, null, null);
    }

    public MQTTClient(String host, int port, String uid, String pwd) throws Exception {
        init(host, port, uid, pwd);
    }

    public MQTTClient(NodeDetails nodeDetails) throws Exception {
        this(nodeDetails.getIp(), nodeDetails.getPort());
    }

    public void init(String host, int port, String uid, String pwd) throws Exception {
        mqtt = new MQTTCli();
        mqtt.setHost(host, port);
        mqtt.setUserName(uid);
        mqtt.setPassword(pwd);
        connection = mqtt.callbackConnection();

    }

    public void subscribe(Long uid, String topic, Callback<byte[]> callback) {
        connection.subscribe(new Topic[]{new Topic(topic, QoS.AT_LEAST_ONCE)}, callback);
    }


    public void unSubscribe(Long uid, String topic, Callback<Void> callback) {
        connection.unsubscribe(new UTF8Buffer[]{new UTF8Buffer(topic)}, callback);
    }


    public Response publish(Long uid, String topic, Request request, Callback<Void> callback) {
        connection.publish(topic, request.toByteArray(), QoS.UNICAST, false, callback);
        return null;
    }


    public Response publishBC(String topic, Request request, Callback<Void> callback) {
        connection.publish(topic, request.toByteArray(), QoS.AT_LEAST_ONCE, false, callback);
        return null;
    }


}
