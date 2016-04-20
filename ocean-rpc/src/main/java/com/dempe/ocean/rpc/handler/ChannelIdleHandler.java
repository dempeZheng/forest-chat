package com.dempe.ocean.rpc.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Sharable
public class ChannelIdleHandler extends ChannelHandlerAdapter {

    private static Logger LOGGER = Logger.getLogger(ChannelIdleHandler.class.getName());

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                LOGGER.log(Level.WARNING, "write idle on channel:" + ctx.channel());
            } else if (e.state() == IdleState.READER_IDLE) {
                LOGGER.log(Level.WARNING, "channel:" + ctx.channel()
                        + " is time out." + ctx.channel().remoteAddress());
                ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
                ctx.close();
            }
        }
    }


}
