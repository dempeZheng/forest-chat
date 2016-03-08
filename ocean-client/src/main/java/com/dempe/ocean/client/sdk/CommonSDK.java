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

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
public class CommonSDK {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonSDK.class);

    protected Long uid;

    protected String pwd;

    protected HABusCliService haBusCliService;


    Thread d_thread;

    protected MessageListener listener;

    public CommonSDK(Long uid, String pwd, MessageListener listener) throws Exception {
        this(uid, pwd);
        this.listener = listener;
    }

    public CommonSDK(Long uid, String pwd) throws Exception {
        this.uid = uid;
        this.pwd = pwd;
        haBusCliService = new HABusCliService(R.FOREST_BUS_NAME);
    }

    protected void init() {
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
                        System.out.println("++++++++++++++++++++++++++++topic>>>>>>>>>" + topic);
                        LOGGER.info("receive message topic:{}", topic);
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


    public void publish(BusMessage message) {
        haBusCliService.publish("", message);
    }

    /**
     * 单播协议
     *
     * @param daemonName
     * @param request
     */
    public void publish(String daemonName, Request request) {
        BusMessage message = new BusMessage();
        message.setDaemonName(daemonName);
        message.setMsgType(MsgType.UNICAST.getValue());
        message.setJsonByteReq(request.toByteArray());
        haBusCliService.publish("", message);
    }

    /**
     * 广播协议
     *
     * @param topic
     * @param request
     */
    public void publishSubBC(String topic, Request request) {
        BusMessage message = new BusMessage();
        message.setDaemonName(R.FOREST_LEAF_NAME);
        message.setMsgType(MsgType.BCSUBCH.getValue());
        message.setJsonByteReq(request.toByteArray());
        haBusCliService.publish(topic, message);
    }

    public void connect(Long uid, String pwd) throws Exception {
        haBusCliService.connect(String.valueOf(uid), pwd);
    }

    public void subscribe(String topic) {
        haBusCliService.subscribe(topic);
    }


    public Future<Message> receive() {
        return haBusCliService.receive();
    }

    public MessageListener getListener() {
        return listener;
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }
}
