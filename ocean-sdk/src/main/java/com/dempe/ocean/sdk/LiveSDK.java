package com.dempe.ocean.sdk;

import com.dempe.ocean.client.NoAvailableClientException;
import com.dempe.ocean.common.protocol.Message;
import org.fusesource.mqtt.client.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 直播类业务sdk
 * User: Dempe
 * Date: 2016/2/26
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class LiveSDK extends CommonSDK {

    private final static Logger LOGGER = LoggerFactory.getLogger(LiveSDK.class);

    public LiveSDK(Long uid, String pwd, MessageListener listener) throws Exception {
        super(uid, pwd, listener);
    }

    public LiveSDK(Long uid, String pwd) throws Exception {
        super(uid, pwd);
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
        haBusCliService.connect(String.valueOf(uid), pwd);

        // 订阅频道
        haBusCliService.subscribe(topSid + "|" + subSid);

        init();
    }


    public Future<org.fusesource.mqtt.client.Message> receive() throws NoAvailableClientException {
        return haBusCliService.receive();
    }

    public static void main(String[] args) throws Exception {
        Long uid = 777888L;
        String pwd = "666666";
        Long topSid = 123L;
        Long subSid = 123L;

        final LiveSDK liveSDK = new LiveSDK(uid, pwd);

        // set listener 监听频道内消息
        liveSDK.setListener(new MessageListener() {
            @Override
            public void onPublish(Message request) {
                LOGGER.info("request>>>>>>>>>>>>>>{}", request);
            }
        });
        // 进频道(建立连接，并订阅频道topic)
        liveSDK.inChannel(topSid, subSid);



        String topic = topSid + "|" + subSid;
        Message request = new Message();
        request.setUid(uid);
        request.setTopic(topic);
        request.setUri("/sample/hello");

        // 发送全频道广播
        liveSDK.publishSubBC(topic, request);

        // 发送单播消息到LEAF_DAEMON_NAME服务器
//        liveSDK.publish(topic, request);
    }
}
