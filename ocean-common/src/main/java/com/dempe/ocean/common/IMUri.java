package com.dempe.ocean.common;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public enum IMUri {

    USER_INIT("/user/imInit"),//申请好友
    FRIEND_APPLY("/friend/apply"),//申请好友
    FRIEND_DEL("/friend/del"),//删除好友
    FRIEND_ACCEPT("/friend/accept"),//接受好友申请
    USER_GROUP_SAVE("userGroup/save"),//创建群
    ;
    private String uri;

    private IMUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
