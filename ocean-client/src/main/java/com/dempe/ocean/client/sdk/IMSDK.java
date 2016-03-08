package com.dempe.ocean.client.sdk;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.MsgType;
import com.dempe.ocean.common.R;
import com.dempe.ocean.common.protocol.BusMessage;
import com.dempe.ocean.common.protocol.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IM聊天类业务SDK
 * User: Dempe
 * Date: 2016/3/8
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class IMSDK extends CommonSDK {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMSDK.class);

    private RequestBuilder builder = new RequestBuilder(R.FOREST_LEAF_NAME);

    public IMSDK(Long uid, String pwd, MessageListener listener) throws Exception {
        super(uid, pwd, listener);
    }

    public IMSDK(Long uid, String pwd) throws Exception {
        super(uid, pwd);
    }

    /**
     * 接受申请
     *
     * @param uid
     * @param friendUid
     */
    public void acceptFriend(Long uid, Long friendUid) {
        JSONObject param = new JSONObject();
        param.put("uid", uid);
        param.put("friendUid", friendUid);
        BusMessage message = builder.buildMessage(uid, param.toJSONString(), IMUri.FRIEND_ACCEPT);
        publish(message);
    }

    /**
     * 申请好友
     *
     * @param uid
     * @param friendUid
     * @param applyMsg
     */
    public void applyFriend(Long uid, Long friendUid, String applyMsg) {
        JSONObject param = new JSONObject();
        param.put("uid", uid);
        param.put("friendUid", friendUid);
        param.put("applyMsg", applyMsg);
        BusMessage message = builder.buildMessage(uid, param.toJSONString(), IMUri.FRIEND_APPLY);
        publish(message);
    }

    /**
     * 删除好友
     *
     * @param uid
     * @param friendUid
     */
    public void delFriend(Long uid, Long friendUid) {
        JSONObject param = new JSONObject();
        param.put("uid", uid);
        param.put("friendUid", friendUid);
        BusMessage message = builder.buildMessage(uid, param.toJSONString(), IMUri.FRIEND_DEL);
        publish(message);
    }

    /**
     * 给好友发送消息
     *
     * @param friendUid
     * @param message
     */
    public void sendMessage(Long friendUid, String message) {
        BusMessage busMessage = new BusMessage();
        busMessage.setDaemonName(R.FOREST_LEAF_NAME);
        busMessage.setMsgType(MsgType.UNICAST.getValue());
        Request request = new Request();
        request.setParam(message);
        request.setTopic(String.valueOf(friendUid));
        request.setUid(String.valueOf(uid));
        request.setUri("/groupMessage");
        busMessage.setJsonByteReq(request.toByteArray());
        haBusCliService.publish(String.valueOf(friendUid), busMessage);
    }

    /**
     * 给群组发送消息
     *
     * @param groupId
     * @param message
     */
    public void sendGroupMessage(Integer groupId, String message) {
        BusMessage busMessage = new BusMessage();
        busMessage.setDaemonName(R.FOREST_BUS_NAME);
        busMessage.setMsgType(MsgType.UNICAST.getValue());
        Request request = new Request();
        request.setParam(message);
        request.setTopic(String.valueOf(groupId));
        request.setUid(String.valueOf(uid));
        request.setUri("/groupMessage");
        busMessage.setJsonByteReq(request.toByteArray());
        haBusCliService.publishBC(String.valueOf(groupId), busMessage);
    }


    public static void main(String[] args) throws Exception {
        Long uid = 66666L;
        String pwd = "666666";

        final IMSDK imsdk = new IMSDK(uid, pwd);

        imsdk.connect(uid,pwd);


        // set listener 监听频道内消息
        imsdk.setListener(new MessageListener() {
            @Override
            public void onPublish(Request request) {
                LOGGER.info("request>>>>>>>>>>>>>>{}", request);
            }
        });

        imsdk.init();


        imsdk.sendMessage(uid, "hello");
    }

}
