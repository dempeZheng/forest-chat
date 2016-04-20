package com.dempe.chat.common;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 框架启动基类
 * User: Dempe
 * Date: 2015/10/15
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */

public class BootServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap b;
    private DefaultEventExecutorGroup executorGroup;
    private ChannelInitializer channelInitializer;


    public BootServer() {
        init();
    }

    public BootServer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
        init();
    }


    public void init() {
        executorGroup = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors(),
                new DefaultThreadFactory("decode-worker-thread-pool"));
        init(channelInitializer);

    }


    public void start() throws IOException {
        try {
            ChannelFuture f = b.bind(8888).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            stop();
        }
    }

    public void init(ChannelInitializer channelInitializer) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_KEEPALIVE, config.soKeepAlive())
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(channelInitializer);
    }


    public void stop() throws IOException {
        if (bossGroup != null)
            bossGroup.shutdownGracefully();
        if (workerGroup != null)
            workerGroup.shutdownGracefully();

    }

    public BootServer stopWithJVMShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stop();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }));
        return this;
    }

}
