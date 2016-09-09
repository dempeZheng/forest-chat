package com.dempe.ocean.http.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.logic.api.UserService;
import com.dempe.ocean.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * IM user API
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

    @RequestMapping("/login")
    @ResponseBody
    public User getUser(@RequestParam String uid, @RequestParam String pwd) {
        return userService.getUser(uid, pwd);
    }

}
