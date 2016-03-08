package com.dempe.ocean.node.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.anno.Param;
import com.dempe.ocean.common.anno.Path;
import com.dempe.ocean.common.utils.JsonResult;
import com.dempe.ocean.node.im.service.FriendService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
@Controller("friend")
public class FriendController {

    @Resource
    private FriendService friendService;

    /**
     * 接受申请
     *
     * @param uid
     * @param friendUid
     */
    @Path
    public JSONObject accept(@Param Long uid, @Param Long friendUid) {

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
    @Path
    public JSONObject apply(@Param Long uid, @Param Long friendUid, @Param String applyMsg) {
        friendService.applyFriend(uid, friendUid, applyMsg);
        return JsonResult.getJsonResult("");
    }

    /**
     * 删除好友
     *
     * @param uid
     * @param friendUid
     */
    @Path
    public JSONObject del(Long uid, Long friendUid) {
        friendService.delFriend(uid, friendUid);
        return JsonResult.getJsonResult("");
    }

}
