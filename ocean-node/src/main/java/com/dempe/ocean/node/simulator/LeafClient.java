package com.dempe.ocean.node.simulator;

import com.dempe.ocean.client.ha.DefaultClientService;
import com.dempe.ocean.common.R;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class LeafClient {
    public static void main(String[] args) throws Exception {
        DefaultClientService clientService = new DefaultClientService(R.FOREST_LEAF_NAME);
        Request request = new Request();
        request.setUri("/user/login");
        request.setUid(222L);
        request.setTopic("2222");
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("uid", "2222");
        paramMap.put("pwd", "ssss");
        request.setParamMap(paramMap);
        Response response = clientService.send(request).await();
        System.out.println(response);
    }
}
