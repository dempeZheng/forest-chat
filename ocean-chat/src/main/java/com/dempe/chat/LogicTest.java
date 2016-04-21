package com.dempe.chat;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.logic.common.action.UserAction;
import com.dempe.ocean.rpc.client.RPCClient;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/21
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class LogicTest {

    public static void main(String[] args) {
        UserAction client = RPCClient.proxyBuilder(UserAction.class)
                .withServerNode("127.0.0.1", 8888)
                .build();
        System.out.println(client);
        JSONObject hello = client.login("222", "");

        System.out.println(">>>>>>>>>>>>>>>>" + hello);
    }
}
