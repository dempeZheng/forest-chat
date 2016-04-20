package com.dempe.chat.common;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/12
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public enum TopicType {
    GROUP("g"),//for 群组
    FRIEND("f"),// for朋友
    MYSELF("m"),//for自己，属于问答模式
    SYS("s"),//系统内部消息，包括syn，push
    OTHER("o");//其他
    private String type;

    private TopicType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
