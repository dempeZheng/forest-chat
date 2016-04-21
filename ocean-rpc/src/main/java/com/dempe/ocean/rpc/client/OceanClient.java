package com.dempe.ocean.rpc.client;

import com.dempe.ocean.rpc.client.DefaultClient;
import com.dempe.ocean.rpc.client.Future;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import com.dempe.ocean.rpc.transport.protocol.ProtocolConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/21
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
public class OceanClient extends DefaultClient {
    public OceanClient(String host, int port) {
        super(host, port);
    }


    public Callback send(byte[] data, String serviceName, String methodName, int compressType,Callback<PacketData> callback) throws Exception {
        PacketData packet = createPacket(serviceName, methodName, compressType, data);
        return send(packet,callback);
    }

    PacketData createPacket(String serviceName, String methodName, int compressType, byte[] data) {
        if (StringUtils.isNotBlank(serviceName) && StringUtils.isNotBlank(methodName)) {
            throw new IllegalArgumentException("method:" + methodName + "serviceName is null,please setClazz first");
        }
        PacketData packetData = new PacketData();
        packetData.magicCode(ProtocolConstant.MAGIC_CODE);
        packetData.compressType(compressType);
        packetData.serviceName(serviceName);
        packetData.methodName(methodName);
        packetData.data(data);
        return packetData;
    }

}
