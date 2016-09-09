package com.dempe.ocean.sdk.example;

import com.dempe.ocean.sdk.Callback;
import com.dempe.ocean.sdk.ChatSdk;
import org.fusesource.mqtt.client.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/21
 * Time: 15:49
 * To change this template use File | Settings | File Templates.
 */
public class ChatSdkDemo {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChatSdk.class);


    private final static String DEF_UID = "222";
    private final static String DEF_PWD = "pwd";

    public static void main(String[] args) throws Exception {
        ChatSdk sdk = new ChatSdk("localhost", 9999, DEF_UID, DEF_PWD, new Callback() {
            @Override
            public void onResponse(String topic, byte[] payload) {
                LOGGER.info("onResponse topci:{},payload:{}", topic, payload);
            }

            @Override
            public void onFriendMsg(String topic, byte[] payload) {
                LOGGER.info("onFriendMsg topci:{},payload:{}", topic, payload);
            }

            @Override
            public void onGroupMsg(String topic, byte[] payload) {
                LOGGER.info("onGroupMsg topci:{},payload:{}", topic, payload);
            }

            @Override
            public void onSysMsg(String topic, byte[] payload) {
                LOGGER.info("onSysMsg topci:{},payload:{}", topic, payload);
            }
        });
        sdk.connect();
        sdk.start();
        // 点对点消息


        while (true) {
            // 发送给自己的消息，
            //
            sdk.publishToAlias(DEF_UID, "hello".getBytes());
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
