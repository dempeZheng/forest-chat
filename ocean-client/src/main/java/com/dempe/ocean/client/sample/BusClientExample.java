package com.dempe.ocean.client.sample;

import com.dempe.ocean.client.bus.cluster.HABusCliService;
import com.dempe.ocean.common.pack.Unpack;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
import org.fusesource.mqtt.client.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/25
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public class BusClientExample {

    private final static Logger LOGGER = LoggerFactory.getLogger(BusClientExample.class);

    static String uid = "8888";
    static String pwd = "66666";
    static String topic = "live_channel";

    public static void main(String[] args) throws Exception {
        HABusCliService haBusCliService = new HABusCliService("forest_bus");


        haBusCliService.connect(uid, pwd);
        haBusCliService.subscribe(topic);

        haBusCliService.publish(topic, getRequest());
        haBusCliService.publish(topic, getRequest());
        haBusCliService.publish(topic, getRequest());
        haBusCliService.publish(topic, getRequest());
        haBusCliService.publish(topic, getRequest());

        while (true) {
            Message message = haBusCliService.receive().await();
            byte[] payload = message.getPayload();
            Response response = new Response();
            response.unmarshal(new Unpack(payload));
            LOGGER.info(">>>>>>>>>>>>>>>>>>>>topic:{},resp:{}", message.getTopic(), response.toString());
            TimeUnit.SECONDS.sleep(1);
            haBusCliService.publish(topic, getRequest());
        }


    }

    public static Request getRequest() {
        Request request = new Request();
        request.setUid(uid);
        request.setTopic(topic);
        request.setName("forest_leaf");
        request.setUri("/sample/hello");
        return request;
    }
}
