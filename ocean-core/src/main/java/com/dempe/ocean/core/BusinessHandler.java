package com.dempe.ocean.core;

import com.dempe.ocean.common.protocol.Request;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 里层业务逻辑层handler
 * User: Dempe
 * Date: 2016/2/23
 * Time: 10:06
 * To change this template use File | Settings | File Templates.
 */
public class BusinessHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(BusinessHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("msg:{}", msg.toString());
        if (msg instanceof Request) {
            Request req = (Request) msg;
            LOGGER.info("req>>>>>>>>>>>>>>>>>>>>>{}", req);

        }

    }


}

