package com.dempe.ocean.node.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.anno.Param;
import com.dempe.ocean.common.anno.Path;
import com.dempe.ocean.common.utils.JsonResult;
import com.dempe.ocean.node.im.service.UserGroupService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
@Controller("userGroup")
public class UserGroupController {

    @Resource
    private UserGroupService userGroupService;

    /**
     * 创建群
     *
     * @param createByUid
     * @param groupName
     * @param groupProfile
     */
    @Path
    public JSONObject save(@Param Long createByUid, @Param String groupName, @Param String groupProfile) {
        userGroupService.saveUserGroup(createByUid, groupName, groupProfile);
        return JsonResult.getJsonResult("");
    }
}
