package com.dempe.ocean.common.protocol;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.pack.Unpack;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class Request {

    private String name;

    private short msgType;

    private String uid;

    private String topic;

    private String uri;

    private String param;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public JSONObject paramJSON() {
        if (StringUtils.isBlank(param)) {
            return new JSONObject();
        }
        return JSONObject.parseObject(param);
    }

    public short getMsgType() {
        return msgType;
    }

    public void setMsgType(short msgType) {
        this.msgType = msgType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void putParaJSON(JSONObject paramJSON) {
        this.param = paramJSON.toJSONString();
    }

    public Pack marshal(Pack pack) {
        pack.putVarstr(name);
        pack.putShort(msgType);
        pack.putVarstr(uid);
        pack.putVarstr(topic);
        pack.putVarstr(uri);
        pack.putVarstr(param);
        return pack;
    }

    public Request unmarshal(Unpack unpack) throws IOException {
        name = unpack.popVarstr();
        msgType = unpack.popShort();
        uid = unpack.popVarstr();
        topic = unpack.popVarstr();
        uri = unpack.popVarstr();
        param = unpack.popVarstr();
        return this;
    }

    @Override
    public String toString() {
        return "Request{" +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
