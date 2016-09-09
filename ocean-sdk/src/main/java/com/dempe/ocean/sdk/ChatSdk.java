package com.dempe.ocean.sdk;

import com.dempe.ocean.common.R;
import com.dempe.ocean.common.TopicType;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/21
 * Time: 14:55
 * To change this template use File | Settings | File Templates.
 */
public class ChatSdk {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChatSdk.class);

    private FutureConnection connection;

    private String uid;
    private String pwd;
    private String clientID;// 由客户端维护唯一性

    private MQTT mqtt;

    private Thread d_thread;
    private Callback callback;


    public ChatSdk(String host, int port, String uid, String pwd, Callback callback) throws Exception {
        this(host, port, uid, pwd, uid, callback);
    }

    public ChatSdk(String host, int port, String uid, String pwd, String clientID, Callback callback) throws Exception {
        this.uid = uid;
        this.pwd = pwd;
        this.clientID = clientID;
        this.callback = callback;
        init(host, port);

    }

    public void start() {
       d_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Future<Message> receive = connection.receive();
                        Message message = receive.await();
                        String topic = message.getTopic();
                        byte[] payload = message.getPayload();
                        if (StringUtils.isBlank(topic)) {
                            // 规定如果是null topic，则为内置协议
                            callback.onSysMsg(topic, payload);
                        } else if (StringUtils.startsWith(topic, TopicType.FRIEND.getType())) {
                            // 发给朋友的消息
                            callback.onFriendMsg(topic, payload);
                        } else if (StringUtils.startsWith(topic, TopicType.GROUP.getType())) {
                            // 发给群组的消息
                            callback.onGroupMsg(topic, payload);
                        } else if (StringUtils.startsWith(topic, TopicType.MYSELF.getType())) {
                            // 发给自己的，属于传统的问答模式的消息，这类消息需要直接透传到逻辑层，交由逻辑层处理
                            callback.onResponse(topic, payload);
                        }
                        message.ack();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                }
            }
        });
        d_thread.setName("MessageThread");
        d_thread.setDaemon(true);
        d_thread.start();
    }


    public void init(String host, int port) throws Exception {
        mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(uid);
        mqtt.setPassword(pwd);
        mqtt.setClientId(uid);
    }

    public FutureConnection connect() throws Exception {
        this.connection = mqtt.futureConnection();
        Future<Void> connect = connection.connect();
        return connection;
    }

    /**
     * @param topic
     * @param payload
     * @return
     */
    public Future<Void> publish(String topic, final byte[] payload) {
        topic = TopicType.FRIEND.getType() + R.SPLIT + topic;
        return connection.publish(topic, payload, QoS.AT_LEAST_ONCE, false);
    }


    /**
     * 发送消息到群组
     *
     * @param groupId
     * @param payload
     * @return
     */
    public Future<Void> publishToGroup(String groupId, final byte[] payload) {
        String topic = TopicType.GROUP.getType() + R.SPLIT + groupId;
        return connection.publish(topic, payload, QoS.AT_LEAST_ONCE, false);
    }

    /**
     * 点对点发给朋友的消息
     *
     * @param toUid
     * @param payload
     * @return
     */
    public Future<Void> publishToAlias(String toUid, final byte[] payload) {
        String topic = TopicType.FRIEND.getType() + R.SPLIT + toUid;
        return connection.publish(topic, payload, QoS.AT_LEAST_ONCE, false);
    }

    /**
     * 发送问答类请求
     *
     * @param serviceName
     * @param methodName
     * @param compressType
     * @param payload
     * @return
     */
    public Future<Void> send(String serviceName, String methodName, int compressType, final byte[] payload) {
        String topic = TopicType.MYSELF.getType() + R.SPLIT + serviceName + R.SPLIT + methodName + R.SPLIT + compressType;
        return connection.publish(topic, payload, QoS.AT_LEAST_ONCE, false);
    }

    /**
     * 发送问答类请求
     *
     * @param serviceName
     * @param methodName
     * @param payload
     * @return
     */
    public Future<Void> send(String serviceName, String methodName, final byte[] payload) {
        String topic = TopicType.MYSELF.getType() + R.SPLIT + serviceName + R.SPLIT + methodName;
        return connection.publish(topic, payload, QoS.AT_LEAST_ONCE, false);
    }


    public FutureConnection getConnection() {
        return connection;
    }


}
