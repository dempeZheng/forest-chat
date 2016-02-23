package com.dempe.ocean.common.codec;

import com.dempe.ocean.common.protocol.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 10:03
 * To change this template use File | Settings | File Templates.
 */
public class RequestEncoder extends MessageToByteEncoder<Request> {


    private final static Logger LOGGER = LoggerFactory.getLogger(RequestEncoder.class);

    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;


    /**
     * @param channelHandlerContext
     * @param request
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Request request, ByteBuf byteBuf) throws Exception {

    }

}
