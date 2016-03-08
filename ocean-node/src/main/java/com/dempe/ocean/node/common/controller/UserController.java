package com.dempe.ocean.node.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.anno.Param;
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
@Controller
public class UserController {

    @Resource
    private UserService userService;

    public JSONObject login(@Param String uid, @Param String pwd) {
        return JsonResult.getJsonResult(userService.login(uid, pwd));
    }
}
