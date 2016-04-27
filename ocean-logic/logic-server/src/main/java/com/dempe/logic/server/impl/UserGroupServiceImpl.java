package com.dempe.logic.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.dempe.logic.api.UserGroupService;
import com.dempe.logic.server.bussiness.UserGroupBusiness;
import com.dempe.ocean.db.model.im.UserGroup;
import com.dempe.ocean.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/10
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupBusiness userGroupBusiness;

    public JSONObject listUidByGroupId(String groupId) {
        return JsonResult.getJsonResult(userGroupBusiness.listUidByGroupId(groupId));
    }

    public JSONObject saveUserGroup(Long uid, String groupId) {
        UserGroup userGroup = new UserGroup();
        userGroup.setCreateTime(System.currentTimeMillis());
        userGroup.setUid(uid);
        userGroup.setGroupId(groupId);
        userGroupBusiness.saveUserGroup(userGroup);
        return JsonResult.getJsonResult(0);
    }

    public JSONObject delUserGroup(String groupId, Long uid) {
        userGroupBusiness.delUserGroup(groupId, uid);
        return JsonResult.getJsonResult(0);
    }


}
