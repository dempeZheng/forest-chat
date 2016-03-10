package com.dempe.ocean.client.service;

import com.dempe.ocean.client.ha.DefaultClientService;
import com.dempe.ocean.client.ha.DefaultHAClient;
import com.dempe.ocean.common.URI;
import com.dempe.ocean.common.cluster.HAProxy;
import com.dempe.ocean.common.protocol.Message;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * im业务java调用service
 * 封装im 常用的业务接口
 * <p/>
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class IMServiceClient extends DefaultClientService {

    public IMServiceClient(String daemonName) throws Exception {
        super(daemonName);
    }

    public IMServiceClient(String name, HAProxy.Strategy strategy, long period) throws Exception {
        super(name, strategy, period);
    }

    public IMServiceClient(DefaultHAClient haClient) {
        super(haClient);
    }

//    USER_INIT("/user/imInit"),//im初始化协议
//    FRIEND_APPLY("/friend/apply"),//申请好友
//    FRIEND_DEL("/friend/del"),//删除好友
//    FRIEND_ACCEPT("/friend/accept"),//接受好友申请
//    USER_GROUP_SAVE("userGroup/save"),//创建群

    public Message login(Long uid, String pwd) throws Exception {
        Message request = new Message();
        request.setTopic("");
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("pwd", pwd);
        request.setUid(uid);
        request.setUri(URI.IM.USER_LOGIN.getUri());
        return sendAndWait(request);
    }


}
