package com.dempe.ocean.node;

import com.dempe.ocean.common.AbstractAcceptor;
import com.dempe.ocean.common.OceanConfig;
import com.dempe.ocean.common.PipelineInitializer;
import com.dempe.ocean.common.codec.DefaultEncoder;
import com.dempe.ocean.common.codec.RequestDecoder;
import com.dempe.ocean.core.IdleTimeoutHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


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

    static class WebSocketFrameToByteBufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

        @Override
        protected void decode(ChannelHandlerContext chc, BinaryWebSocketFrame frame, List<Object> out) throws Exception {
            //convert the frame to a ByteBuf
            ByteBuf bb = frame.content();
            //System.out.println("WebSocketFrameToByteBufDecoder decode - " + ByteBufUtil.hexDump(bb));
            bb.retain();
            out.add(bb);
        }
    }

    static class ByteBufToWebSocketFrameEncoder extends MessageToMessageEncoder<ByteBuf> {

        @Override
        protected void encode(ChannelHandlerContext chc, ByteBuf bb, List<Object> out) throws Exception {
            //convert the ByteBuf to a WebSocketFrame
            BinaryWebSocketFrame result = new BinaryWebSocketFrame();
            //System.out.println("ByteBufToWebSocketFrameEncoder encode - " + ByteBufUtil.hexDump(bb));
            result.content().writeBytes(bb);
            out.add(result);
        }
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
        final IdleTimeoutHandler timeoutHandler = new IdleTimeoutHandler();
        String host = config.host();
        int port = config.port();
        initFactory(host, port, new PipelineInitializer() {
            @Override
            public void init(ChannelPipeline pipeline) {
//                pipeline.addFirst("idleStateHandler", new IdleStateHandler(0, 0, Constants.DEFAULT_CONNECT_TIMEOUT));
//                pipeline.addAfter("idleStateHandler", "idleEventHandler", timeoutHandler);
                //pipeline.addLast("logger", new LoggingHandler("Netty", LogLevel.ERROR));
                pipeline.addLast("requestDecoder", new RequestDecoder());
                pipeline.addLast("encode", new DefaultEncoder());
                pipeline.addLast("ProcessorHandler", handlerAdapter);
            }
        });
    }


}
