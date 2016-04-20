package com.dempe.ocean.rpc.client;


import com.dempe.ocean.rpc.client.proxy.CglibProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(RPCClient.class);

    static RPCClient instance;

    public static RPCClient getInstance() {
        if (instance == null) {
            synchronized (RPCClient.class) {
                if (instance == null) {
                    instance = new RPCClient();
                }
            }
        }
        return instance;
    }


    public static <T> ObjProxyBuilder<T> proxyBuilder(Class<T> clazz) {
        return new ObjProxyBuilder<T>(clazz);
    }


    public static class ObjProxyBuilder<T> {
        private Class<T> clazz;
        private String host;
        private int port;

        public ObjProxyBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ObjProxyBuilder<T> withServerNode(String host, int port) {
            this.host = host;
            this.port = port;
            return this;
        }

        public T build() {
            CglibProxy<T> proxy = new CglibProxy<T>(host, port);
            proxy.setClazz(clazz);
            T t = (T) proxy.getProxy();
            return t;
        }

        public CglibProxy buildAsyncObjPrx() {
            CglibProxy<T> proxy = new CglibProxy<T>(host, port);
            proxy.setClazz(clazz);
            return proxy;
        }


    }


}


