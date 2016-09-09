package com.dempe.logic.api;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/27
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public interface UserGroupService {

    JSONObject listUidByGroupId(String groupId);

    JSONObject saveUserGroup(Long uid, String groupId);

    JSONObject delUserGroup(String groupId, Long uid);

}
