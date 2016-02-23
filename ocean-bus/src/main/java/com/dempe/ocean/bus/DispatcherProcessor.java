package com.dempe.ocean.bus;

import com.dempe.ocean.client.cluster.HAClientService;
import com.dempe.ocean.common.protocol.Request;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherProcessor {

    private final static Map<String, HAClientService> nameClientMap = Maps.newConcurrentMap();


    public void dispatcher(Channel channel, Request request) throws Exception {
        HAClientService clientService = getClientServiceByName(request.getName());
        clientService.sendAndWrite(channel, request);
    }

    /**
     * 根据节点名称获取对应的HAClientService
     * HAClientService 会选择路由策略选择合适的业务进程，将消息透传
     *
     * @param name
     * @return
     * @throws Exception
     */
    private HAClientService getClientServiceByName(String name) throws Exception {
        HAClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new HAClientService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }
}
