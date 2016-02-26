package com.dempe.ocean.node.simulator;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.client.node.cluster.HANodeCliService;
import com.dempe.ocean.common.Constants;
import com.dempe.ocean.common.protocol.Request;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class LeafSimulator {

    public static void main(String[] args) throws Exception {
        HANodeCliService clientService = new HANodeCliService(Constants.FOREST_LEAF_NAME);
        for (int i = 0; i < 10000; i++) {
            Request request = buildReq();
            clientService.sendOnly(request);
            if (i % 10 == 0) {
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    public static Request buildReq() {
        Request request = new Request();
        request.setUri("/sample/hello");
        JSONObject param = new JSONObject();
        param.put("name", "dempe");
        request.putParaJSON(param);
        return request;
    }
}
