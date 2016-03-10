package com.dempe.ocean.node;


import com.dempe.ocean.common.OceanConfig;
import com.dempe.ocean.common.PipelineInitializer;
import com.dempe.ocean.common.codec.DefaultEncoder;
import com.dempe.ocean.common.codec.MessageDecoder;
import com.dempe.ocean.common.register.NameDiscoveryService;
import com.dempe.ocean.node.frame.ProcessorHandler;
import com.dempe.ocean.node.frame.ServerContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

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
    ApplicationContext context;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap b;
    private DefaultEventExecutorGroup executorGroup;
    private OceanConfig config;
    private ServerContext servercontext;


    public BootServer(OceanConfig config, ApplicationContext context) throws IOException {
        this.config = config;
        this.context = context;
        servercontext = new ServerContext(config, context);
        init();
    }


    private void init() throws IOException {
        String host = config.host();
        int port = config.port();
        initFactory(host, port, new PipelineInitializer() {
            @Override
            public void init(ChannelPipeline pipeline) {
                //pipeline.addLast("logger", new LoggingHandler("Netty", LogLevel.ERROR));
                pipeline.addLast("requestDecoder", new MessageDecoder());
                pipeline.addLast("encode", new DefaultEncoder());
                pipeline.addLast("ProcessorHandler", new ProcessorHandler(servercontext));
            }
        });
    }

    public void initFactory(String host, int port, final PipelineInitializer pipeliner) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        try {
                            pipeliner.init(pipeline);
                        } catch (Throwable th) {
                            LOGGER.error("Severe error during pipeline creation", th);
                            throw th;
                        }
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }


    public void start() {
        try {
            ChannelFuture f = b.bind(config.port()).sync();


            LOGGER.info("server start:{}", config.port());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            stop();
        }
    }


    public BootServer registerNameService() throws Exception {
        NameDiscoveryService forestNameService = new NameDiscoveryService();
        forestNameService.start();
        forestNameService.register();
        return this;
    }


    public void stop() {
        if (bossGroup != null)
            bossGroup.shutdownGracefully();
        if (workerGroup != null)
            workerGroup.shutdownGracefully();
    }

    public BootServer stopWithJVMShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stop();
            }
        }));
        return this;
    }


}
