package com.dempe.ocean.common.protocol;

import com.dempe.ocean.common.pack.Marshallable;
import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.pack.Unpack;

import java.io.IOException;
import java.util.Arrays;

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

    private byte[] jsonByteReq;


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

    public byte[] getJsonByteReq() {
        return jsonByteReq;
    }

    public void setJsonByteReq(byte[] jsonByteReq) {
        this.jsonByteReq = jsonByteReq;
    }

    public Pack marshal(Pack pack) {
        pack.putShort(msgType);
        pack.putVarstr(daemonName);
        pack.putFetch(jsonByteReq);
        return pack;
    }

    public BusMessage unmarshal(Unpack unpack) throws IOException {
        msgType = unpack.popShort();
        daemonName = unpack.popVarstr();
        int remaining = unpack.getOriBuffer().remaining();
        jsonByteReq = unpack.popFetch(remaining);
        return this;
    }


    public byte[] toByteArray() {
        return this.marshal(new Pack()).getBuffer().array();
    }

    @Override
    public String toString() {
        return "BusReq{" +
                "msgType=" + msgType +
                ", daemonName='" + daemonName + '\'' +
                ", jsonByteReq=" + Arrays.toString(jsonByteReq) +
                '}';
    }
}
