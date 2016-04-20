package com.dempe.ocean.logic.im.action;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.db.model.im.UserGroup;
import com.dempe.ocean.logic.im.service.UserGroupService;
import com.dempe.ocean.rpc.RPCService;
import com.dempe.ocean.rpc.RPCMethod;
import com.dempe.ocean.rpc.utils.JsonResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/10
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
@Component
@RPCService
public class UserGroupAction {

    @Resource
    private UserGroupService userGroupService;

    @RPCMethod
    public JSONObject listUidByGroupId(String groupId) {
        return JsonResult.getJsonResult(userGroupService.listUidByGroupId(groupId));
    }

    @RPCMethod
    public JSONObject saveUserGroup(Long uid, String groupId) {
        UserGroup userGroup = new UserGroup();
        userGroup.setCreateTime(System.currentTimeMillis());
        userGroup.setUid(uid);
        userGroup.setGroupId(groupId);
        userGroupService.saveUserGroup(userGroup);
        return JsonResult.getJsonResult(0);
    }

    @RPCMethod
    public JSONObject delUserGroup(String groupId, Long uid) {
        userGroupService.delUserGroup(groupId, uid);
        return JsonResult.getJsonResult(0);
    }


}
