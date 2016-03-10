package com.dempe.ocean.client.ha;

import com.dempe.ocean.client.Callback;
import com.dempe.ocean.client.Client;
import com.dempe.ocean.client.Future;
import com.dempe.ocean.client.Promise;
import com.dempe.ocean.common.cluster.HAProxy;
import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class DefaultClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultClientService.class);

    private DefaultHAClient defaultHAClient;

    public DefaultClientService(String name) throws Exception {
        this(name, HAProxy.Strategy.DEFAULT, 1000);
    }

    public DefaultClientService(String name, HAProxy.Strategy strategy, long period) throws Exception {
        if (defaultHAClient == null) {
            synchronized (DefaultClientService.class) {
                if (defaultHAClient == null) {
                    defaultHAClient = new DefaultHAClient(name, strategy, period);
                }
            }
        }
    }

    public DefaultClientService(DefaultHAClient haClient) {
        this.defaultHAClient = haClient;
    }

    /**
     * 基于回调的send
     *
     * @param request
     * @param callback
     * @return
     * @throws Exception
     */
    public Callback send(Request request, Callback callback) throws Exception {
        Client client = defaultHAClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.send(request, callback);
    }

    /**
     * 基于future的send
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Future<Response> send(Request request) throws Exception {
        Promise<Response> future = new Promise<Response>();
        send(request, future);
        return future;
    }

    /**
     * 同步的send
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Response sendAndWait(Request request) throws Exception {
        return send(request).await();
    }

    public Response sendAndWait(Request request, long amount, TimeUnit unit) throws Exception {
        return send(request).await(amount, unit);
    }


}
