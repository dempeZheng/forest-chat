package com.dempe.ocean.rpc.client;


import com.dempe.ocean.rpc.transport.protocol.PacketData;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class DefaultClient extends CommonClient {

    public DefaultClient(String host, int port) {
        super(host, port);
    }

    private int nextMessageId = 1;
    private long getNextMessageId() {
        long rc = nextMessageId;
        nextMessageId++;
        if (nextMessageId == 0) {
            nextMessageId = 1;
        }
        return rc;
    }

    public void sendOnly(PacketData request) throws Exception {
        writeAndFlush(request);
    }

    /**
     * 发送消息，并等待Response
     *
     * @param request
     * @return Response
     */
    public Callback send(PacketData request, Callback callback) throws Exception {
        long id = getNextMessageId();
        request.correlationId(id);
        Context context = new Context(id, request, callback);
        contextMap.put(id, context);
        writeAndFlush(request);
        return callback;
    }

    public Future<PacketData> send(PacketData request) throws Exception {
        Promise<PacketData> future = new Promise<PacketData>();
        send(request, future);
        return future;
    }


    public PacketData sendAnWait(PacketData request) throws Exception {
        Future<PacketData> future = send(request);
        return future.await();
    }

    public PacketData sendAnWait(PacketData request, long amount, TimeUnit unit) throws Exception {
        Future<PacketData> future = send(request);
        return future.await(amount, unit);
    }


}
