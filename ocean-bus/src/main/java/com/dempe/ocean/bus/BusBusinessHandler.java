package com.dempe.ocean.bus;

import com.dempe.ocean.common.protocol.BusMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class BusBusinessHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(BusBusinessHandler.class);

    DispatcherProcessor processor;

    public BusBusinessHandler(DispatcherProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("msg:{}", msg.toString());
        if (msg instanceof BusMessage) {
            BusMessage req = (BusMessage) msg;

            LOGGER.info("dispatcher msg to {}", req.getDaemonName());
            processor.dispatcher(req.getDaemonName(), req.getJsonByteReq());

        }

    }


}

