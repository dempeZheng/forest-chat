package com.dempe.ocean.common.codec;

import com.dempe.ocean.common.pack.ProtocolValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/26
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public class ByteArrayEncoder extends MessageToByteEncoder<byte[]> {


    private final static Logger LOGGER = LoggerFactory.getLogger(ByteArrayEncoder.class);

    /**
     * @param channelHandlerContext
     * @param bytes
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, byte[] bytes, ByteBuf byteBuf) throws Exception {
        try {
            int len = bytes.length + 4;
            int nFirstValue = ProtocolValue.combine(len, 0);
            byteBuf = byteBuf.order(ByteOrder.LITTLE_ENDIAN);
            byteBuf.writeInt(nFirstValue);
            byteBuf.writeBytes(bytes);
        } catch (Throwable e) {
            LOGGER.error("throwable: " + e.getMessage(), e);
            throw new EncoderException(e);
        }
    }

}

