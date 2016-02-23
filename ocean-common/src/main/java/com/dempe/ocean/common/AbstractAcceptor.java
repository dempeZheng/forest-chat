package com.dempe.ocean.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAcceptor {


    private static final Logger LOG = LoggerFactory.getLogger(AbstractAcceptor.class);

    EventLoopGroup m_bossGroup;
    EventLoopGroup m_workerGroup;

    public abstract void initialize(OceanConfig config) throws IOException;

    public void initFactory(String host, int port, final PipelineInitializer pipeliner) {
        m_bossGroup = new NioEventLoopGroup();
        m_workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(m_bossGroup, m_workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        try {
                            pipeliner.init(pipeline);
                        } catch (Throwable th) {
                            LOG.error("Severe error during pipeline creation", th);
                            throw th;
                        }
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(host, port);
            LOG.info("Server binded host: {}, port: {}", host, port);
            f.sync();
        } catch (InterruptedException ex) {
            LOG.error(null, ex);
        }
    }


    public void close() {
        if (m_workerGroup == null) {
            throw new IllegalStateException("Invoked close on an Acceptor that wasn't initialized");
        }
        if (m_bossGroup == null) {
            throw new IllegalStateException("Invoked close on an Acceptor that wasn't initialized");
        }
        Future workerWaiter = m_workerGroup.shutdownGracefully();
        Future bossWaiter = m_bossGroup.shutdownGracefully();

        try {
            workerWaiter.await(100);
        } catch (InterruptedException iex) {
            throw new IllegalStateException(iex);
        }

        try {
            bossWaiter.await(100);
        } catch (InterruptedException iex) {
            throw new IllegalStateException(iex);
        }

    }


}
