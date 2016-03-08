package com.dempe.ocean.sdk;

import com.dempe.ocean.common.MsgType;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Request;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class RequestBuilder {
    private String daemonName;

    public RequestBuilder(String daemonName) {
        this.daemonName = daemonName;
    }

    public BusMessage buildMessage(Long uid, String param, IMUri imUri) {
        Request request = new Request();
        request.setUri(imUri.getUri());
        request.setUid(String.valueOf(uid));
        request.setData(param);
        BusMessage busMessage = new BusMessage();
        busMessage.setDaemonName(daemonName);
        busMessage.setMsgType(MsgType.UNICAST.getValue());
        return busMessage;
    }

}
