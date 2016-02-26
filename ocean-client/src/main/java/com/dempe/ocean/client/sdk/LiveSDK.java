package com.dempe.ocean.client.sdk;

import com.dempe.ocean.client.bus.cluster.HABusCliService;
import com.dempe.ocean.common.MsgType;
import com.dempe.ocean.common.pack.Unpack;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Request;
import org.apache.commons.lang3.RandomStringUtils;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

/**
 * 直播类业务sdk
 * User: Dempe
 * Date: 2016/2/26
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class LiveSDK {

    private final static Logger LOGGER = LoggerFactory.getLogger(LiveSDK.class);

    private String uid;
    private String pwd;
    private final static String BUS_DAEMON_NAME = "forest_bus";
    private final static String LEAF_DAEMON_NAME = "forest_leaf";

    private HABusCliService haBusCliService;

    public LiveSDK(String uid, String pwd) throws Exception {
        this.uid = uid;
        this.pwd = pwd;
        haBusCliService = new HABusCliService(BUS_DAEMON_NAME);
    }

    /**
     * 用户进频道
     *
     * @param topSid
     * @param subSid
     */
    public void inChannel(Long topSid, Long subSid) throws Exception {
        // 建议连接
        haBusCliService.connect(uid, pwd);

        // 订阅频道
        haBusCliService.subscribe(topSid + "|" + subSid);
    }

    public void publish(Long topSid, Long subSid, String daemonName, Request request) {
        BusMessage message = new BusMessage();
        message.setDaemonName(daemonName);
        message.setMsgType(MsgType.UNICAST.getValue());
        message.setJsonByteReq(request.toByteArray());
        haBusCliService.publish(getTopic(topSid, subSid), message);
    }
    public void publishSubBC(Long topSid, Long subSid, String daemonName, Request request) {
        BusMessage message = new BusMessage();
        message.setDaemonName(daemonName);
        message.setMsgType(MsgType.BCSUBCH.getValue());
        message.setJsonByteReq(request.toByteArray());
        haBusCliService.publish(getTopic(topSid, subSid), message);
    }

    public Future<Message> receive() {
        return haBusCliService.receive();
    }

    private String getTopic(Long topSid, Long subSid) {
        return topSid + "|" + subSid;
    }

    public static void main(String[] args) throws Exception {
        String uid = UUID.randomUUID().toString();
        String pwd = "666666";
        Long topSid = 123L;
        Long subSid = 123L;

        LiveSDK liveSDK = new LiveSDK(uid, pwd);
        liveSDK.inChannel(topSid, subSid);

        Request request = new Request();
        request.setUid(uid);
        request.setTopic(topSid + "|" + subSid);
        request.setUri("/sample/hello");
        liveSDK.publishSubBC(topSid, subSid, LEAF_DAEMON_NAME, request);
        while (true) {
            Future<Message> receive = liveSDK.receive();
            Message message = receive.await();
            String topic = message.getTopic();
            byte[] payload = message.getPayload();
            Request req = new Request().unmarshal(new Unpack(payload));

            LOGGER.info(">>>>>>>>>request:{}", req);
            // 根据request处理业务逻辑


        }
    }
}
