package com.dempe.ocean.client.bus;

import com.dempe.ocean.client.NoAvailableClientException;
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
    public void subscribe(String topic) throws NoAvailableClientException;


    /**
     * 用户退出直播间
     *
     * @param topic
     */
    public void unSubscribe(String topic) throws NoAvailableClientException;

    /**
     * 发布消息
     *
     * @param topic
     * @param message
     * @return
     */
    public void publish(String topic, BusMessage message) throws NoAvailableClientException;



    /**
     * 发布消息
     *
     * @param topic
     * @param bytes
     * @return
     */
    public void publish(String topic, byte[] bytes) throws NoAvailableClientException;


    public Future<Message> receive() throws NoAvailableClientException;


}
