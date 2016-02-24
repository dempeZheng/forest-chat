package com.dempe.ocean.bus;

import com.dempe.ocean.client.cluster.HAClientService;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.core.spi.persistence.UidSessionStore;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
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





    public void dispatcher(Channel channel, String name, ByteBuffer byteBuf) throws Exception {
        if (StringUtils.isBlank(name)) {
            return;
        }
        HAClientService clientService = getClientServiceByName(name);
        UidSessionStore.put("555", channel);
        clientService.sendBuffer(byteBuf);


    }

    public void dispatcher(Channel channel, String name, Request request) throws Exception {
        if (StringUtils.isBlank(name)) {
            return;
        }
        HAClientService clientService = getClientServiceByName(name);
        UidSessionStore.put("555", channel);
        clientService.sendOnly(request);

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
