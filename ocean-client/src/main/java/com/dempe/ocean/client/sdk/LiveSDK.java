package com.dempe.ocean.client.sdk;

import com.dempe.ocean.client.bus.cluster.HABusCliService;
import com.dempe.ocean.common.MsgType;
import com.dempe.ocean.common.R;
import com.dempe.ocean.common.pack.Unpack;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Request;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    private HABusCliService haBusCliService;

    Thread d_thread;

    private MessageListener listener;

    public LiveSDK(String uid, String pwd, MessageListener listener) throws Exception {
        this(uid, pwd);
        this.listener = listener;
    }

    public LiveSDK(String uid, String pwd) throws Exception {
        this.uid = uid;
        this.pwd = pwd;
        haBusCliService = new HABusCliService(R.FOREST_BUS_NAME);
    }

    private void init() {
        d_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // 收频道内消息
                    Future<Message> receive = haBusCliService.receive();
                    Message message = null;
                    try {
                        message = receive.await();
                        String topic = message.getTopic();
                        LOGGER.debug("receive message topic:{}", topic);
                        byte[] payload = message.getPayload();
                        Request req = new Request().unmarshal(new Unpack(payload));
                        if (listener == null) {
                            return;
                        }
                        listener.onPublish(req);


                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                }
            }
        });
        d_thread.setDaemon(true);
        d_thread.start();
    }


    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    /**
     * 用户进频道
     *
     * @param topSid
     * @param subSid
     */
    public void inChannel(Long topSid, Long subSid) throws Exception {
        // 建议连接
        // TODO 未考虑异常情况
        haBusCliService.connect(uid, pwd);

        // 订阅频道
        haBusCliService.subscribe(topSid + "|" + subSid);

        init();
    }

    /**
     * 直播间单播协议
     *
     * @param topSid
     * @param subSid
     * @param daemonName
     * @param request
     */
    public void publish(Long topSid, Long subSid, String daemonName, Request request) {
        BusMessage message = new BusMessage();
        message.setDaemonName(daemonName);
        message.setMsgType(MsgType.UNICAST.getValue());
        message.setJsonByteReq(request.toByteArray());
        haBusCliService.publish(getTopic(topSid, subSid), message);
    }

    /**
     * 直播间广播协议
     *
     * @param topSid
     * @param subSid
     * @param daemonName
     * @param request
     */
    public void publishSubBC(Long topSid, Long subSid, String daemonName, Request request) {
        BusMessage message = new BusMessage();
        message.setDaemonName(daemonName);
        message.setMsgType(MsgType.BCSUBCH.getValue());
        message.setJsonByteReq(request.toByteArray());
        haBusCliService.publish(getTopic(topSid, subSid), message);
    }

    public void publishSubBC(Long topSid, Long subSid, Request request) {
        BusMessage message = new BusMessage();
        message.setDaemonName(R.FOREST_LEAF_NAME);
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

        final LiveSDK liveSDK = new LiveSDK(uid, pwd);

        // set listener 监听频道内消息
        liveSDK.setMessageListener(new MessageListener() {
            @Override
            public void onPublish(Request request) {
                LOGGER.info("request>>>>>>>>>>>>>>{}", request);
            }
        });
        // 进频道(建立连接，并订阅频道topic)
        liveSDK.inChannel(topSid, subSid);

        Request request = new Request();
        request.setUid(uid);
        request.setTopic(topSid + "|" + subSid);
        request.setUri("/sample/hello");

        // 发送全频道广播
        liveSDK.publishSubBC(topSid, subSid, request);

        // 发送单播消息到LEAF_DAEMON_NAME服务器
        liveSDK.publish(topSid, subSid, R.FOREST_LEAF_NAME, request);
    }
}
