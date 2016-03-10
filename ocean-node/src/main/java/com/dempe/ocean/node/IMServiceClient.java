package com.dempe.ocean.node;

import com.dempe.ocean.client.ha.DefaultClientService;
import com.dempe.ocean.common.IMUri;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class IMServiceClient {

    private DefaultClientService clientService;

    public IMServiceClient(String daemonName) throws Exception {
        clientService = new DefaultClientService(daemonName);
    }

    public Response login(Long uid, String topic, String pwd) throws Exception {
        Request request = new Request();
        request.setTopic(topic);
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("pwd", pwd);
        request.setUid(uid);
        request.setUri(IMUri.FRIEND_DEL.getUri());
        return clientService.sendAndWait(request);
    }

}
