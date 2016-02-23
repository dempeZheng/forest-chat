package com.dempe.ocean.client;

import com.dempe.ocean.common.NodeDetails;
import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.protocol.Request;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;


/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class MQTTClient {
    private String host;
    private int port;

    private CallbackConnection connection;

    public MQTTClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        init();
    }

    public MQTTClient(NodeDetails nodeDetails) throws Exception {
        this(nodeDetails.getIp(), nodeDetails.getPort());
    }

    public void init() throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);
        connection = mqtt.callbackConnection();
        connection.listener(new EventListener());

    }

    public void send(String topic,Request request) {
        Pack pack = new Pack();
        request.marshal(pack);
        byte[] array = pack.getBuffer().array();
        connection.publish(topic, array, QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }


}
