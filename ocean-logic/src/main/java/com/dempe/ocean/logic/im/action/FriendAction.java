package com.dempe.ocean.logic.im.action;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.logic.im.service.FriendService;
import com.dempe.ocean.rpc.RPCService;
import com.dempe.ocean.rpc.RPCMethod;
import com.dempe.ocean.rpc.utils.JsonResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
@Component
@RPCService
public class FriendAction {

    @Resource
    private FriendService friendService;

    /**
     * 接受申请
     *
     * @param uid
     * @param friendUid
     */
    @RPCMethod
    public JSONObject accept(Long uid, Long friendUid) {
        friendService.acceptFriend(uid, friendUid);
        return JsonResult.getJsonResult("");
    }


    /**
     * 申请好友
     *
     * @param uid
     * @param friendUid
     * @param applyMsg
     */
    @RPCMethod
    public JSONObject apply(Long uid, Long friendUid, String applyMsg) {
        friendService.applyFriend(uid, friendUid, applyMsg);
        return JsonResult.getJsonResult("");
    }

    /**
     * 删除好友
     *
     * @param uid
     * @param friendUid
     */
    public JSONObject del(Long uid, Long friendUid) {
        friendService.delFriend(uid, friendUid);
        return JsonResult.getJsonResult("");
    }

}
