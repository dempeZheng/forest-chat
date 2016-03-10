package com.dempe.ocean.client;


import com.dempe.ocean.common.protocol.Message;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public interface Client {


    public void sendOnly(Message request) throws Exception;

    /**
     * 发送消息
     *
     * @param request
     * @return Response
     */
    public Callback send(Message request, Callback callback) throws Exception;


}
