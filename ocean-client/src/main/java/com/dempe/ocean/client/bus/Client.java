package com.dempe.ocean.client.bus;

import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Response;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    public void connect(String uid, String pwd) throws Exception;

    /**
     * 用户订阅主题
     *
     * @param topic
     */
    public void subscribe(String topic);


    /**
     * 用户退出直播间
     *
     * @param topic
     */
    public void unSubscribe(String topic);

    /**
     * 发布消息
     *
     * @param topic
     * @param request
     * @return
     */
    public Response publish(String topic, BusMessage request);


    /**
     * 发布消息
     *
     * @param topic
     * @param bytes
     * @return
     */
    public Response publish(String topic, byte[] bytes);


    public Future<Message> receive();


}
