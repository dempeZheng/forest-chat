package com.dempe.ocean.bus.sample;


import com.dempe.ocean.client.bus.mqtt.*;
import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.protocol.Request;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class MQTTClientExample {

    public static void main(String[] args) throws Exception {
        MQTTCli mqtt = new MQTTCli();
        mqtt.setHost("localhost", 1883);
        String uid = "12345";
        mqtt.setUserName(uid);
        mqtt.setPassword("123");
        mqtt.setClientId(uid);

        FutureConnection connection = mqtt.futureConnection();
        connection.connect();

        BlockingConnection blockingConnection = mqtt.blockingConnection();
        blockingConnection.receive();

        Request request = new Request();
        request.setName("forest_leaf");
        request.setUri("/sample/hello");
        Pack pack = new Pack();
        request.marshal(pack);
        ByteBuffer buffer = pack.getBuffer();
        buffer.flip();
        byte[] array = buffer.array();
        Topic[] topics = {new Topic("foo", QoS.AT_LEAST_ONCE)};
        Future<byte[]> subscribe = connection.subscribe(topics);
        System.out.println("qoese>>>" + new String(subscribe.await()));

//        connection.publish("foo", array, QoS.UNICAST, false);
        Future<Void> foo = connection.publish("foo", array, QoS.UNICAST, false);


        System.out.println("-----------------------------");


        while (true) {
            Message message = connection.receive().await();
            System.out.println(message.getTopic());
            byte[] payload = message.getPayload();

            System.out.println("payload>>>" + new String(payload));
            // process the message then:
            message.ack();
            Thread.sleep(1000);
        }


    }
}
