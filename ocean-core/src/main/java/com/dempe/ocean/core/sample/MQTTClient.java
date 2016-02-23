package com.dempe.ocean.core.sample;

import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.protocol.Request;
import org.fusesource.mqtt.client.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class MQTTClient {

    public static void main(String[] args) throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);

        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();


        Request request = new Request();
        request.setName("leaf");
        request.setUri("/test");
        Pack pack = new Pack();
        request.marshal(pack);
        byte[] array = pack.getBuffer().array();
        connection.publish("foo", array, QoS.AT_LEAST_ONCE, false);

        Topic[] topics = {new Topic("foo", QoS.AT_LEAST_ONCE)};
        byte[] qoses = connection.subscribe(topics);

        System.out.println("qoese>>>" + new String(qoses));
        while (true) {
            Message message = connection.receive();
            System.out.println(message.getTopic());
            byte[] payload = message.getPayload();

            System.out.println("payload>>>" + new String(payload));
            // process the message then:
            message.ack();
            Thread.sleep(1000);
        }


    }
}
