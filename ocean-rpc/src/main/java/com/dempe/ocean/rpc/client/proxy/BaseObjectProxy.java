package com.dempe.ocean.rpc.client.proxy;

import com.dempe.ocean.rpc.client.DefaultClient;
import com.dempe.ocean.rpc.RPCService;
import com.dempe.ocean.rpc.RPCMethod;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import com.dempe.ocean.rpc.transport.protocol.ProtocolConstant;
import com.dempe.ocean.rpc.utils.ArgsCodec;
import com.dempe.ocean.rpc.utils.PathUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class BaseObjectProxy<T> extends DefaultClient {

    protected Class<T> clazz;

    protected String serviceName;

    private Map<String, PacketData> packetCache = Maps.newHashMap();

    public BaseObjectProxy(String host, int port) {
        super(host, port);
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
        RPCService rpcExporter = clazz.getAnnotation(RPCService.class);
        if (rpcExporter == null) {
            new IllegalArgumentException("clazz :" + clazz.getSimpleName() + " ,RPCExporter is null");
        }
        this.serviceName = rpcExporter.serviceName();
        if (StringUtils.isBlank(serviceName)) {
            serviceName = clazz.getSimpleName();
        }
    }

    PacketData createPacket(Method method, Object[] args,RPCMethod rpcMethod) {
        String methodName = method.getName();
        if (serviceName == null) {
            throw new IllegalArgumentException("method:" + methodName + "serviceName is null,please setClazz first");
        }
        String serviceKey = PathUtil.buildPath(serviceName, method.getName());
        PacketData packetData = packetCache.get(serviceKey);
        if (packetData == null) {
            String funcName = rpcMethod.methodName();
            if (StringUtils.isBlank(funcName)) {
                funcName = method.getName();
            }
            packetData = new PacketData();
            packetData.magicCode(ProtocolConstant.MAGIC_CODE);
            packetData.compressType(rpcMethod.compressType().value());
            packetData.serviceName(serviceName);
            packetData.methodName(funcName);
            packetCache.put(serviceKey, packetData.copy());
        }

        if (args != null && args.length > 0) {
            byte[] bytes = ArgsCodec.encodeArgs(args);
            if (bytes != null) {
                packetData.data(bytes);
            }
        }
        return packetData;
    }


}
