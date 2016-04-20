package com.dempe.ocean.logic.live.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.logic.live.service.LiveChannelService;
import com.dempe.ocean.rpc.RPCService;
import com.dempe.ocean.rpc.RPCMethod;
import com.dempe.ocean.rpc.utils.JsonResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
@Component
@RPCService
public class LiveChannelAction {

    @Resource
    private LiveChannelService liveChannelService;


    @RPCMethod
    public JSONObject delChannel(Long topSid, Long subSid) {
        liveChannelService.delChannel(topSid, subSid);
        return JsonResult.getJsonResult("");
    }

    @RPCMethod
    public JSONObject saveChannel(Long uid, Long topSid, Long subSid, String name, String profile) {
        liveChannelService.saveChannel(uid, topSid, subSid, name, profile);
        return JsonResult.getJsonResult("");
    }


}
