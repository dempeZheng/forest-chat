package com.dempe.ocean.http.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.logic.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/9/9
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/imInit")
    public String imInit() {
        JSONObject jsonObject = userService.imInit();
        return jsonObject.toJSONString();
    }

}
