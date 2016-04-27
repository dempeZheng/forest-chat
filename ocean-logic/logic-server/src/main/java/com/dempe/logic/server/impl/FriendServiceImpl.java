package com.dempe.logic.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.dempe.logic.api.FriendService;
import com.dempe.logic.server.bussiness.FriendBusiness;
import com.dempe.ocean.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendBusiness friendBusiness;

    /**
     * 接受申请
     *
     * @param uid
     * @param friendUid
     */
    public JSONObject accept(Long uid, Long friendUid) {
        friendBusiness.acceptFriend(uid, friendUid);
        return JsonResult.getJsonResult("");
    }


    /**
     * 申请好友
     *
     * @param uid
     * @param friendUid
     * @param applyMsg
     */
    public JSONObject apply(Long uid, Long friendUid, String applyMsg) {
        friendBusiness.applyFriend(uid, friendUid, applyMsg);
        return JsonResult.getJsonResult("");
    }

    /**
     * 删除好友
     *
     * @param uid
     * @param friendUid
     */
    public JSONObject del(Long uid, Long friendUid) {
        friendBusiness.delFriend(uid, friendUid);
        return JsonResult.getJsonResult("");
    }

}
