package com.dempe.ocean.logic;


import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.logic.common.action.UserAction;
import com.dempe.ocean.rpc.client.Future;
import com.dempe.ocean.rpc.client.RPCClient;
import com.dempe.ocean.rpc.client.proxy.CglibProxy;
import com.dempe.ocean.rpc.transport.protocol.PacketData;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class LeafSimulator {
    public static void main(String[] args) throws Exception {
        test2();

//        CglibProxy<UserController> proxy = new CglibProxy<UserController>("127.0.0.1", 8888);
//        proxy.setClazz(UserController.class);
//        UserController userController = (UserController) proxy.getProxy();
//        System.out.println(userController.login("222", ""));
    }


    public static void test2() {
        UserAction client = RPCClient.proxyBuilder(UserAction.class)
                .withServerNode("127.0.0.1", 8888)
                .build();
        System.out.println(client);
        JSONObject hello = client.login("222", "");

        System.out.println(">>>>>>>>>>>>>>>>" + hello);
    }

    public static void test() throws Exception {
        CglibProxy objectProxy = RPCClient.proxyBuilder(UserAction.class)
                .withServerNode("127.0.0.1", 8888)
                .buildAsyncObjPrx();
        Future<PacketData> hello = objectProxy.call("login", "222", "");
        PacketData await = hello.await();
        byte[] data = await.getData();
        System.out.println(new String(data));
    }


}
