package com.dempe.ocean.bus.sample;

import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.pack.Unpack;
import com.dempe.ocean.common.protocol.Request;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/24
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        Request request = new Request();
        request.setName("leaf");
        request.setUri("/sample/test");
        Pack pack = new Pack();
        request.marshal(pack);
        ByteBuffer buffer = pack.getBuffer();
        buffer.flip();
        byte[] array = buffer.array();


        Unpack unpack = new Unpack(array);
        String s = unpack.popVarstr();
        System.out.println(s);
    }
}
