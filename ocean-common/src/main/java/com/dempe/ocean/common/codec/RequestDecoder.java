package com.dempe.ocean.common.codec;

import com.dempe.ocean.common.pack.Unpack;
import com.dempe.ocean.common.protocol.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class RequestDecoder extends MessageToMessageDecoder {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestDecoder.class);


    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        if (msg instanceof ByteBuffer) {
            // 解码 request
            ByteBuffer buf = (ByteBuffer) msg;
            Unpack unpack = new Unpack(buf.array());
            Request request = new Request();
            request.unmarshal(unpack);
            out.add(request);
        }
    }
}
