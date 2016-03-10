package com.dempe.ocean.common.protocol;

import com.dempe.ocean.common.pack.Marshallable;
import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.pack.Unpack;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/26
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public class BusMessage implements Marshallable {

    // 消息类型{广播，单播，多播}
    private short msgType;

    // 消息路由节点进程名
    private String daemonName;

    private Message request = new Message();

    public short getMsgType() {
        return msgType;
    }

    public void setMsgType(short msgType) {
        this.msgType = msgType;
    }

    public String getDaemonName() {
        return daemonName;
    }

    public void setDaemonName(String daemonName) {
        this.daemonName = daemonName;
    }

    public Message getRequest() {
        return request;
    }

    public void setRequest(Message request) {
        this.request = request;
    }

    public Pack marshal(Pack pack) {
        pack.putShort(msgType);
        pack.putVarstr(daemonName);
        pack.putMarshallable(request);
        return pack;
    }

    public BusMessage unmarshal(Unpack unpack) throws IOException {
        msgType = unpack.popShort();
        daemonName = unpack.popVarstr();
        request = (Message) unpack.popMarshallable(new Message());
        return this;
    }


    public byte[] toByteArray() {
        return this.marshal(new Pack()).getBuffer().array();
    }


}
