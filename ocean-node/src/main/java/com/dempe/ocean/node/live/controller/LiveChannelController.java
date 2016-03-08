package com.dempe.ocean.node.live.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.anno.Param;
import com.dempe.ocean.common.anno.Path;
import com.dempe.ocean.common.utils.JsonResult;
import com.dempe.ocean.node.live.service.LiveChannelService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
@Controller("liveChannel")
public class LiveChannelController {

    @Resource
    private LiveChannelService liveChannelService;


    @Path
    public JSONObject delChannel(@Param Long topSid, @Param Long subSid) {
        liveChannelService.delChannel(topSid, subSid);
        return JsonResult.getJsonResult("");
    }

    @Path
    public JSONObject saveChannel(@Param Long uid, @Param Long topSid, @Param Long subSid, @Param String name, @Param String profile) {
        liveChannelService.saveChannel(uid, topSid, subSid, name, profile);
        return JsonResult.getJsonResult("");
    }


}
