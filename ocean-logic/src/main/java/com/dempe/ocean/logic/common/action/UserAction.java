package com.dempe.ocean.logic.common.action;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.db.model.User;
import com.dempe.ocean.logic.common.service.UserService;
import com.dempe.ocean.rpc.RPCService;
import com.dempe.ocean.rpc.RPCMethod;
import com.dempe.ocean.rpc.utils.JsonResult;
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
@RPCService
public class UserAction {

    @Resource
    private UserService userService;

    @RPCMethod
    public JSONObject login(String uid, String pwd) {
        User login = userService.login(uid, pwd);
        // 4 test
        if (login == null) {
            login = new User();
            login.setClientID(uid);
            login.setPwd(pwd);
        }
        return JsonResult.getJsonResult(login);
    }


    @RPCMethod
    public JSONObject imInit() {
        JSONObject result = new JSONObject();
        result.put("init", "success");
        return JsonResult.getJsonResult(result);
    }

    @RPCMethod
    public JSONObject chInit() {
        JSONObject result = new JSONObject();
        result.put("init", "success");
        return JsonResult.getJsonResult(result);
    }

}
