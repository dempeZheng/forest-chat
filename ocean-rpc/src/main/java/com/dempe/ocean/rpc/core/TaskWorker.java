package com.dempe.ocean.rpc.core;

import com.dempe.ocean.rpc.transport.protocol.PacketData;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务处理线程类
 * User: Dempe
 * Date: 2015/12/11
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class TaskWorker implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskWorker.class);

    private ChannelHandlerContext ctx;
    private ServerContext context;
    private PacketData request;

    public TaskWorker(ChannelHandlerContext ctx, ServerContext context, PacketData request) {
        this.ctx = ctx;
        this.context = context;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            // set执行上下文环境
            context.setLocalContext(request, ctx);
            ActionTake tack = new ActionTake(context);
            final PacketData act = tack.act(request);
            if (act != null) {
                // 交给netty io 线程处理
                ctx.executor().submit(new Runnable() {
                    @Override
                    public void run() {
                        ctx.writeAndFlush(act);
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            // remove执行上下文环境
            context.removeLocalContext();
        }

    }
}
