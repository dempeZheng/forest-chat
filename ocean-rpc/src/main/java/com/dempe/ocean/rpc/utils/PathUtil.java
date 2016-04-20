package com.dempe.ocean.rpc.utils;

import com.dempe.ocean.rpc.transport.protocol.PacketData;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class PathUtil {
    public static String buildPath(String serviceName, String method) {
        return serviceName + "|" + method;
    }

    public static String buildPath(PacketData packetData) {
        return buildPath(packetData.getRpcMeta().getRequest().getServiceName(),
                packetData.getRpcMeta().getRequest().getMethodName());
    }
}
