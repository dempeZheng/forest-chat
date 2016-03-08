package com.dempe.ocean.bus;

import com.dempe.ocean.client.node.cluster.HANodeCliService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherProcessor {

    private final static Map<String, HANodeCliService> nameClientMap = Maps.newConcurrentMap();

    public void dispatcher(String name, byte[] bytes) throws Exception {
        if (StringUtils.isBlank(name)) {
            return;
        }
        HANodeCliService clientService = getClientServiceByName(name);
        clientService.sendBytes(bytes);
    }

    /**
     * 根据节点名称获取对应的HAClientService
     * HAClientService 会选择路由策略选择合适的业务进程，将消息透传
     *
     * @param name
     * @return
     * @throws Exception
     */
    private HANodeCliService getClientServiceByName(String name) throws Exception {
        HANodeCliService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new HANodeCliService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }


}
