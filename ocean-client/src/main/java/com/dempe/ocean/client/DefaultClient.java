package com.dempe.ocean.client;


import com.dempe.ocean.common.protocol.Request;
import com.dempe.ocean.common.protocol.Response;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class DefaultClient extends CommonClient implements Client {

    private int nextMessageId = 1;

    public DefaultClient(String host, int port) {
        super(host, port);
    }

    private int getNextMessageId() {
        int rc = nextMessageId;
        nextMessageId++;
        if (nextMessageId == 0) {
            nextMessageId = 1;
        }
        return rc;
    }

    public void sendOnly(Request request) throws Exception {
        writeAndFlush(request);
    }

    /**
     * 发送消息，并等待Response
     *
     * @param request
     * @return Response
     */
    public Callback send(Request request, Callback callback) throws Exception {
        int id = getNextMessageId();
        request.setMessageID(id);
        Context context = new Context(id, request, callback);
        contextMap.put(id, context);
        writeAndFlush(request);
        return callback;
    }


    public Future<Response> send(Request request) throws Exception {
        Promise<Response> future = new Promise<Response>();
        send(request, future);
        return future;
    }


    public Response sendAnWait(Request request) throws Exception {
        Future<Response> future = send(request);
        return future.await();
    }

    public Response sendAnWait(Request request, long amount, TimeUnit unit) throws Exception {
        Future<Response> future = send(request);
        return future.await(amount, unit);
    }


}
