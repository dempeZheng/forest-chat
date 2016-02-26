package com.dempe.ocean.client.node;


import com.dempe.ocean.common.protocol.Request;
import io.netty.buffer.ByteBuf;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    public void sendOnly(Request request) throws Exception;

    public void sendBuffer(ByteBuf buffer);

    public void sendBytes(byte[] bytes);

//
}
