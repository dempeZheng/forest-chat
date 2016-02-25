package com.dempe.ocean.node;

import com.dempe.ocean.common.AbstractAcceptor;
import com.dempe.ocean.common.OceanConfig;
import com.dempe.ocean.common.PipelineInitializer;
import com.dempe.ocean.common.codec.DefaultEncoder;
import com.dempe.ocean.common.codec.RequestDecoder;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
public class NodeServerAcceptor extends AbstractAcceptor {

    private ChannelHandlerAdapter handlerAdapter;

    public NodeServerAcceptor(ChannelHandlerAdapter handlerAdapter) {
        this.handlerAdapter = handlerAdapter;
    }

    private static final Logger LOG = LoggerFactory.getLogger(NodeServerAcceptor.class);

    EventLoopGroup m_bossGroup;
    EventLoopGroup m_workerGroup;

    @Override
    public void initialize(OceanConfig config) throws IOException {
        m_bossGroup = new NioEventLoopGroup();
        m_workerGroup = new NioEventLoopGroup();
        initializePlainTCPTransport(config);
    }


    private void initializePlainTCPTransport(OceanConfig config) throws IOException {
        String host = config.host();
        int port = config.port();
        initFactory(host, port, new PipelineInitializer() {
            @Override
            public void init(ChannelPipeline pipeline) {
                //pipeline.addLast("logger", new LoggingHandler("Netty", LogLevel.ERROR));
                pipeline.addLast("requestDecoder", new RequestDecoder());
                pipeline.addLast("encode", new DefaultEncoder());
                pipeline.addLast("ProcessorHandler", handlerAdapter);
            }
        });
    }


}
