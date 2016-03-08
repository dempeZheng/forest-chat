package com.dempe.ocean.node;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.client.node.cluster.HANodeCliService;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class IMServiceClient {

    private HANodeCliService haNodeCliService;

    public IMServiceClient(String daemonName) throws Exception {
        haNodeCliService = new HANodeCliService(daemonName);
    }

    public JSONObject login(Long uid, String pwd) {
        return null;
    }

}
