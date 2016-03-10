package com.dempe.ocean.node.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.anno.Param;
import com.dempe.ocean.common.anno.Path;
import com.dempe.ocean.common.utils.JsonResult;
import com.dempe.ocean.node.common.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
@Controller("user")
public class UserController {

    @Resource
    private UserService userService;

    @Path
    public JSONObject login(@Param String uid, @Param String pwd) {
        return JsonResult.getJsonResult(userService.login(uid, pwd));
    }


    @Path
    public JSONObject imInit() {
        JSONObject result = new JSONObject();
        result.put("init", "success");
        return JsonResult.getJsonResult(result);
    }

    @Path
    public JSONObject chInit() {
        JSONObject result = new JSONObject();
        result.put("init", "success");
        return JsonResult.getJsonResult(result);
    }

}
