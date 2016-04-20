package com.dempe.ocean.rpc.client.proxy;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.rpc.RPCMethod;
import com.dempe.ocean.rpc.client.Callback;
import com.dempe.ocean.rpc.client.Future;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import com.dempe.ocean.rpc.utils.ErrorCodes;
import com.dempe.ocean.rpc.utils.RPCSrvException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * cglib代理，提供性能
 * 暴露service不必强制为interface
 * User: Dempe
 * Date: 2016/4/20
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class CglibProxy<T> extends BaseObjectProxy<T> implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public CglibProxy(String host, int port) {
        super(host, port);
    }

    public Object getProxy() {
        //设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //通过字节码技术动态创建子类实例
        return enhancer.create();
    }

    public Future<PacketData> call(String methodName, Object... args) throws Exception {
        Method method = getMethod(methodName, args);
        RPCMethod rpcMethod = method.getAnnotation(RPCMethod.class);
        if (rpcMethod == null) {
            throw new IllegalArgumentException("method:" + method.getName() + "RPCService is null");
        }
        PacketData packet = createPacket(method, args, rpcMethod);
        return send(packet);
    }

    public void notify(String methodName, Callback<T> callback, Object... args) throws Exception {
        Method method = getMethod(methodName, args);
        RPCMethod rpcMethod = method.getAnnotation(RPCMethod.class);
        if (rpcMethod == null) {
            throw new IllegalArgumentException("method:" + method.getName() + "RPCService is null");
        }
        PacketData packet = createPacket(method, args, rpcMethod);
        send(packet, callback);
    }

    private Method getMethod(String methodName, Object[] args) throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        Method method = clazz.getMethod(methodName, parameterTypes);
        if (method == null) {
            new IllegalArgumentException("method is null for methodName=" + methodName);
        }
        return method;
    }


    //实现MethodInterceptor接口方法
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name) || "hashCode".equals(name) || "toString".equals(name)) {
                return proxy.invokeSuper(obj, args);
            }
        }
        RPCMethod rpcMethod = method.getAnnotation(RPCMethod.class);
        if (rpcMethod == null) {
            throw new IllegalArgumentException("method:" + method.getName() + "RPCService is null");
        }
        PacketData packet = createPacket(method, args, rpcMethod);
        Future<PacketData> send = send(packet);
        // 超时时间
        long timeout = rpcMethod.timeout();
        PacketData packetData = send.await(timeout, TimeUnit.MILLISECONDS);
        Integer errorCode = packetData.getRpcMeta().getResponse().getErrorCode();
        if (ErrorCodes.ST_SUCCESS != errorCode) {
            // 服务端返回状态码非成功状态，抛出异常
            throw new RPCSrvException("rpc srv err: srv code = " + errorCode + ", err msg:"
                    + packetData.getRpcMeta().getResponse().getErrorText());
        }
        byte[] data = packetData.getData();
        JSONObject json = JSONObject.parseObject(new String(data));
        return json;

    }
}
