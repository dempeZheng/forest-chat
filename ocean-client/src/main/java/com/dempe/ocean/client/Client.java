package com.dempe.ocean.client;


import com.dempe.ocean.common.protocol.Request;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    public void sendOnly(Request request) throws Exception;

    public void sendBuffer(ByteBuffer buffer);


//
}
