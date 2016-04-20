package com.dempe.ocean.rpc.handler;

import com.dempe.ocean.rpc.utils.BetterExecutorService;
import com.dempe.ocean.rpc.core.ServerContext;
import com.dempe.ocean.rpc.core.TaskWorker;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务逻辑处理handler
 * User: Dempe
 * Date: 2015/12/10
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProcessorHandler.class);

    // 业务逻辑线程池(业务逻辑最好跟netty io线程分开处理，线程切换虽会带来一定的性能损耗，但可以防止业务逻辑阻塞io线程)
    private final static BetterExecutorService threadPool = new BetterExecutorService(100);

    private ServerContext context;

    public ProcessorHandler(ServerContext context) {
        this.context = context;

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PacketData) {
            threadPool.submit(new TaskWorker(ctx, context, (PacketData) msg));
        }
    }


}
