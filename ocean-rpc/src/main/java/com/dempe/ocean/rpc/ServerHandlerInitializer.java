package com.dempe.ocean.rpc;


import com.dempe.ocean.rpc.core.ServerContext;
import com.dempe.ocean.rpc.handler.PacketDecoder;
import com.dempe.ocean.rpc.handler.PacketEncoder;
import com.dempe.ocean.rpc.handler.ProcessorHandler;
import com.dempe.ocean.rpc.handler.UnCompressHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {


    private ServerContext context;

    public ServerHandlerInitializer(ServerContext context) {
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("PacketDecoder", new PacketDecoder(300 * 1000))
                .addLast("PacketEncoder", new PacketEncoder())
                .addLast("UnCompressHandler", new UnCompressHandler())
                .addLast("ProcessorHandler", new ProcessorHandler(context));
    }

}
