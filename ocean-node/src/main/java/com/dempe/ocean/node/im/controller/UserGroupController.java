package com.dempe.ocean.node.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.anno.Param;
import com.dempe.ocean.common.anno.Path;
import com.dempe.ocean.common.model.im.UserGroup;
import com.dempe.ocean.common.utils.JsonResult;
import com.dempe.ocean.node.im.service.UserGroupService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/10
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class UserGroupController {

    @Resource
    private UserGroupService userGroupService;

    @Path
    public JSONObject listUidByGroupId(@Param String groupId) {
        return JsonResult.getJsonResult(userGroupService.listUidByGroupId(groupId));
    }

    @Path
    public JSONObject saveUserGroup(@Param Long uid, @Param String groupId) {
        UserGroup userGroup = new UserGroup();
        userGroup.setCreateTime(System.currentTimeMillis());
        userGroup.setUid(uid);
        userGroup.setGroupId(groupId);
        userGroupService.saveUserGroup(userGroup);
        return JsonResult.getJsonResult(0);
    }

}
