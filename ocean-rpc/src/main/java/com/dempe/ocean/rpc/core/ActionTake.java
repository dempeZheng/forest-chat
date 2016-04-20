package com.dempe.ocean.rpc.core;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import com.dempe.ocean.rpc.utils.ArgsCodec;
import com.dempe.ocean.rpc.utils.ErrorCodes;
import com.dempe.ocean.rpc.utils.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 业务处理快照类
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class ActionTake implements Take<PacketData, PacketData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionTake.class);

    private ServerContext context;

    public ActionTake(ServerContext context) {
        this.context = context;
    }


    /**
     * 消息处理，一个请求返回一个应答
     *
     * @param packetData 请求消息
     * @return PacketData 返回消息
     */
    public PacketData act(PacketData packetData) {
        String uri = PathUtil.buildPath(packetData);
        // 通过Request uri找到对应的ActionMethod
        ActionMethod actionMethod = context.tackAction(uri);
        if (actionMethod == null) {
            // service method没找到异常
            packetData.errorCode(ErrorCodes.ST_SERVICE_NOTFOUND);
            LOGGER.debug("actionMethod:{} return void.", actionMethod);
            return packetData;
        }
        Method method = actionMethod.getMethod();
        byte[] data = packetData.getData();
        Type[] type = method.getGenericParameterTypes();
        Object[] args = ArgsCodec.decodeArgs(data, type);
        // 获取方法参数
        try {
            Object result = actionMethod.call(args);
            if (result == null) {
                // 这里返回空data数据,快速响应client
                return packetData;
            } else {
                if (result instanceof String) {
                    packetData.data(((String) result).getBytes());
                } else if (result instanceof JSONObject) {
                    packetData.data(JSON.toJSONBytes(result));
                }
                packetData.errorCode(ErrorCodes.ST_SUCCESS);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            // 未知异常
            packetData.errorCode(ErrorCodes.ST_ERROR);
        }
        return packetData;
    }


}
