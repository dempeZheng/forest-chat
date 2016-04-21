package com.dempe.ocean.sdk;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/21
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class ChatCallback implements Callback {
    // 问答消息的返回
    public void onResponse(String topic, byte[] payload) {

    }

    /**
     * 好友消息
     *
     * @param topic
     * @param payload
     */
    public void onFriendMsg(String topic, byte[] payload) {

    }

    /**
     * 消息群消息
     *
     * @param topic
     * @param payload
     */
    public void onGroupMsg(String topic, byte[] payload) {

    }

    /**
     * 系统消息
     *
     * @param topic
     * @param payload
     */
    public void onSysMsg(String topic, byte[] payload) {

    }
}
