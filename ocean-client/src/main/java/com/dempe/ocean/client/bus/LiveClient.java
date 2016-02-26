package com.dempe.ocean.client.bus;

import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Response;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Message;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
public interface LiveClient {

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
     * 直播间业务消息单播
     *
     * @param topic
     * @param request
     * @return
     */
    public Response publish(String topic, BusMessage request);


    /**
     * 直播间业务消息广播
     *
     * @param topic
     * @param request
     * @return
     */
    public Response publishBC(String topic, BusMessage request);


    /**
     * 直播间业务消息多播
     *
     * @param uidList
     * @param topic
     * @param request
     * @return
     */
    public Response publishMultiBC(List<Long> uidList, String topic, BusMessage request);


    public Future<Message> receive();


}
