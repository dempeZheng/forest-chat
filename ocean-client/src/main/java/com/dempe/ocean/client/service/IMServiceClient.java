package com.dempe.ocean.client.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.client.ha.DefaultClientService;
import com.dempe.ocean.client.ha.DefaultHAClient;
import com.dempe.ocean.common.R;
import com.dempe.ocean.common.URI;
import com.dempe.ocean.common.cluster.HAProxy;
import com.dempe.ocean.common.model.User;
import com.dempe.ocean.common.protocol.Message;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(IMServiceClient.class);

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
//    USER_GROUP_SAVE("imGroup/save"),//创建群

    public User login(Long uid, String pwd) throws Exception {
        Message request = new Message();
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("pwd", pwd);
        request.setUid(uid);
        request.setUri(URI.IM.USER_LOGIN.getUri());
        Message response = sendAndWait(request);
        byte[] extendData = response.getExtendData();
        JSONObject result = (JSONObject) JSONObject.parse(extendData);
        if (result.get(R.CODE) != 0) {
            throw new Exception(result.getString(R.MSG));
        }
        return result.getObject(R.DATA, User.class);
    }

    /**
     * 获取订阅topic的用户
     *
     * @param topic
     * @return
     * @throws Exception
     */
    public JSONArray listUidByTopic(String topic) throws Exception {
        Message request = new Message();
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("groupId", topic);
        request.setUri(URI.IM.USER_GROUP_LIST.getUri());
        Message response = sendAndWait(request);
        byte[] extendData = response.getExtendData();
        JSONObject result = (JSONObject) JSONObject.parse(extendData);
        if (result.get(R.CODE) != 0) {
            throw new Exception(result.getString(R.MSG));
        }
        return result.getJSONArray(R.DATA);
    }

    public boolean subscribeTopic(String uid, String topic) throws Exception {
        Message request = new Message();
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("uid", uid);
        paramMap.put("groupId", topic);
        request.setUri(URI.IM.USER_GROUP_LIST.getUri());
        Message response = sendAndWait(request);
        byte[] extendData = response.getExtendData();
        JSONObject result = (JSONObject) JSONObject.parse(extendData);
        if (result.get(R.CODE) != 0) {
            LOGGER.info("msg:{}", result.getString(R.MSG));
            return false;
        }
        return true;
    }


}
