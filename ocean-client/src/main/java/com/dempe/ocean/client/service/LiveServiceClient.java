package com.dempe.ocean.client.service;

import com.dempe.ocean.client.ha.DefaultClientService;
import com.dempe.ocean.client.ha.DefaultHAClient;
import com.dempe.ocean.common.cluster.HAProxy;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/10
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public class LiveServiceClient extends DefaultClientService {
    public LiveServiceClient(String name) throws Exception {
        super(name);
    }

    public LiveServiceClient(String name, HAProxy.Strategy strategy, long period) throws Exception {
        super(name, strategy, period);
    }

    public LiveServiceClient(DefaultHAClient haClient) {
        super(haClient);
    }
}
