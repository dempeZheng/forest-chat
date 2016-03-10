package com.dempe.ocean.common.protocol;

import com.dempe.ocean.common.pack.MarshallUtils;
import com.dempe.ocean.common.pack.Marshallable;
import com.dempe.ocean.common.pack.Pack;
import com.dempe.ocean.common.pack.Unpack;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/22
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class Message implements Marshallable {

    private Integer messageID = 0;//消息id

    private Long uid = 0L;// 用户uid

    private String topic = "";//消息主题

    private String uri = "/";//消息uri

    private Map<String, String> paramMap = Maps.newHashMap();// 参数map

    private byte[] extendData = new byte[0];//扩展数据byte，目前主要用于im中消息的存储


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getExtendData() {
        return extendData;
    }

    public void setExtendData(byte[] extendData) {
        this.extendData = extendData;
    }

    public Pack marshal(Pack pack) {
        pack.putInt(messageID);
        pack.putLong(uid);
        pack.putVarstr(topic);
        pack.putVarstr(uri);
        MarshallUtils.packMap(pack, paramMap, String.class, String.class);
        pack.putBuffer(ByteBuffer.wrap(extendData));
        return pack;
    }

    public Message unmarshal(Unpack unpack) throws IOException {
        messageID = unpack.popInt();
        uid = unpack.popLong();
        topic = unpack.popVarstr();
        uri = unpack.popVarstr();
        paramMap = MarshallUtils.unpackMap(unpack, String.class, String.class, false);
        int remaining = unpack.getOriBuffer().remaining();
        extendData = unpack.popFetch(remaining);
        return this;
    }

    public byte[] toByteArray() {
        return this.marshal(new Pack()).getBuffer().array();
    }

    @Override
    public String toString() {
        return "Request{" +
                "messageID=" + messageID +
                ", uid='" + uid + '\'' +
                ", topic='" + topic + '\'' +
                ", uri='" + uri + '\'' +
                ", paramMap=" + paramMap +
                '}';
    }
}
