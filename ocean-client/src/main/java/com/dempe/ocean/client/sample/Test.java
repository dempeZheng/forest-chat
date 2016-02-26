package com.dempe.ocean.client.sample;

import com.dempe.ocean.client.bus.LiveClient;
import com.dempe.ocean.common.protocol.BusMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/26
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        BusMessage request = BusClientExample.getRequest();
        byte[] bytes = request.toByteArray();
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        wrap = wrap.order(ByteOrder.LITTLE_ENDIAN);
        short msgType = wrap.getShort();
        System.out.println(msgType);
        byte[] arr = new byte[wrap.getShort()];
        wrap.get(arr);
        String daemonName = new String(arr, "UTF-8");
        System.out.println(daemonName);
        System.out.println(wrap);
        wrap.putInt(wrap.position(),10);
        System.out.println(wrap);
        int remaining = wrap.remaining();
        byte[] array = new byte[remaining];
        wrap.get(array);
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
        int messageID = buffer.getInt();
        System.out.println("messageID:"+messageID);
        short len = buffer.getShort();

        byte[] strArr = new byte[len];
        buffer.get(strArr);
        System.out.println(new String(strArr));
        System.out.println(len);
    }
}
