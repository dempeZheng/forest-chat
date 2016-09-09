package com.dempe.logic.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.dempe.logic.api.UserService;
import com.dempe.logic.server.bussiness.UserBusiness;
import com.dempe.ocean.db.model.User;
import com.dempe.ocean.utils.JsonResult;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserServiceImpl implements UserService {

    @Resource
    private UserBusiness userBusiness;


    @Override
    public User login(String uid, String pwd) {
        User user = new User();
        user.setUserName(uid);
        user.setPwd(pwd);
        return user;
    }


    public JSONObject imInit() {
        JSONObject result = new JSONObject();
        result.put("init", "success");
        return JsonResult.getJsonResult(result);
    }

    public JSONObject chInit() {
        JSONObject result = new JSONObject();
        result.put("init", "success");
        return JsonResult.getJsonResult(result);
    }

}
