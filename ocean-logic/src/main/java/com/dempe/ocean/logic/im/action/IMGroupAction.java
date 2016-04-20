package com.dempe.ocean.logic.im.action;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.logic.im.service.IMGroupService;
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
public class IMGroupAction {

    @Resource
    private IMGroupService imGroupService;

    /**
     * 创建群
     *
     * @param createByUid
     * @param groupName
     * @param groupProfile
     */
    @RPCMethod
    public JSONObject save(Long createByUid, String groupName, String groupProfile) {
        imGroupService.saveUserGroup(createByUid, groupName, groupProfile);
        return JsonResult.getJsonResult("");
    }
}
