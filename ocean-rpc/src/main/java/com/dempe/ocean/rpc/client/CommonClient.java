package com.dempe.ocean.rpc.client;


import com.dempe.ocean.rpc.handler.CompressHandler;
import com.dempe.ocean.rpc.handler.PacketDecoder;
import com.dempe.ocean.rpc.handler.PacketEncoder;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class CommonClient {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CommonClient.class);
    protected Bootstrap b;
    protected ChannelPool channelPool;
    protected EventLoopGroup group;
    protected Map<Long, Context> contextMap = Maps.newConcurrentMap();
    private DefaultEventExecutorGroup executorGroup;
    private String host;
    private int port;


    public CommonClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init() {
        b = new Bootstrap();
        group = new NioEventLoopGroup(4);
        executorGroup = new DefaultEventExecutorGroup(4,
                new DefaultThreadFactory("worker group"));
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initClientChannel(ch);
                    }
                });

        channelPool = new ChannelPool(this);
    }

    public void initClientChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestEncoder", new PacketEncoder())
                .addLast("ResponseDecoder", new PacketDecoder(500000))
                .addLast("CompressHandler", new CompressHandler())
                .addLast("ClientHandler", new ChannelHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        long id = 0;
                        PacketData packetData = (PacketData) msg;
                        id = packetData.getRpcMeta().getCorrelationId();

                        Context context = contextMap.remove(id);
                        if (context == null) {
                            LOGGER.debug("messageID:{}, take Context null", id);
                            return;
                        }
                        context.cb.onReceive(packetData);
                    }
                });
    }

    public ChannelFuture connect(final String host, final int port) {
        ChannelFuture f = null;
        try {
            f = b.connect(host, port).sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return f;
    }

    public ChannelFuture connect() {
        ChannelFuture f = null;
        try {
            f = b.connect(host, port).sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return f;
    }

    public void writeAndFlush(Object request) throws Exception {
        Connection connection = channelPool.getChannel();
        connection.doTransport(request);
    }

    public static class Context {
        final PacketData packetData;
        public final Callback cb;
        private final long id;

        Context(long id, PacketData packetData, Callback cb) {
            this.id = id;
            this.cb = cb;
            this.packetData = packetData;
        }
    }


}
