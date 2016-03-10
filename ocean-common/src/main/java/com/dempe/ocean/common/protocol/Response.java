package com.dempe.ocean.common.protocol;

import com.dempe.ocean.common.pack.Marshallable;
import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.pack.Unpack;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class Response implements Marshallable {

    private Integer messageID = 0;
    private Long uid;
    private String topic;
    private String result;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    @Override
    public Pack marshal(Pack pack) {
        pack.putInt(messageID);
        pack.putLong(uid);
        pack.putVarstr(topic);
        pack.putVarstr(result);
        return pack;
    }

    @Override
    public Response unmarshal(Unpack unpack) throws IOException {
        messageID = unpack.popInt();
        uid = unpack.popLong();
        topic = unpack.popVarstr();
        result = unpack.popVarstr();
        return this;
    }

    public byte[] toByteArray() {
        return this.marshal(new Pack()).getBuffer().array();
    }


    @Override
    public String toString() {
        return "Response{" +
                "uid='" + uid + '\'' +
                ", topic='" + topic + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
