package com.dempe.ocean.sdk;

import com.dempe.ocean.common.URI;
import com.dempe.ocean.common.MsgType;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Message;

import java.util.Map;

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

    public BusMessage buildMessage(Long uid, Map<String, String> paramMap, URI.IM imUri) {
        Message request = new Message();
        request.setUri(imUri.getUri());
        request.setUid(uid);
        request.setParamMap(paramMap);
        BusMessage busMessage = new BusMessage();
        busMessage.setDaemonName(daemonName);
        busMessage.setMsgType(MsgType.UNICAST.getValue());
        busMessage.setRequest(request);
        return busMessage;
    }

    public BusMessage buildMessage(Long uid, URI.IM imUri) {
        Message request = new Message();
        request.setUri(imUri.getUri());
        request.setUid(uid);
        BusMessage busMessage = new BusMessage();
        busMessage.setDaemonName(daemonName);
        busMessage.setMsgType(MsgType.UNICAST.getValue());
        busMessage.setRequest(request);
        return busMessage;
    }

}
