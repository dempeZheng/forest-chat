package com.dempe.logic.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.dempe.logic.api.ImGroupService;
import com.dempe.logic.server.bussiness.IMGroupBusiness;
import com.dempe.ocean.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class ImGroupServiceImpl implements ImGroupService {

    @Autowired
    private IMGroupBusiness imGroupBusiness;

    /**
     * 创建群
     *
     * @param createByUid
     * @param groupName
     * @param groupProfile
     */
    public JSONObject save(Long createByUid, String groupName, String groupProfile) {
        imGroupBusiness.saveUserGroup(createByUid, groupName, groupProfile);
        return JsonResult.getJsonResult("");
    }


}
