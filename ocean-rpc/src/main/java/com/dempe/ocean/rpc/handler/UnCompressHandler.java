/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dempe.ocean.rpc.handler;


import com.dempe.ocean.rpc.transport.compress.Compress;
import com.dempe.ocean.rpc.transport.compress.GZipCompress;
import com.dempe.ocean.rpc.transport.compress.SnappyCompress;
import com.dempe.ocean.rpc.transport.protocol.PacketData;
import com.dempe.ocean.rpc.transport.protocol.RpcMeta;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Do data compress handler
 *
 * @author xiemalin
 * @since 1.4
 */
@Sharable
public class UnCompressHandler extends MessageToMessageDecoder<PacketData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(UnCompressHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, PacketData dataPackage, List<Object> out) throws Exception {
        try {
            // check if do compress
            Integer compressType = dataPackage.getRpcMeta().getCompressType();
            Compress compress = null;
            if (compressType == RpcMeta.COMPRESS_GZIP) {
                compress = new GZipCompress();
            } else if (compressType == RpcMeta.COMPRESS_SNAPPY) {
                compress = new SnappyCompress();
            }
            if (compress != null) {
                byte[] data = dataPackage.getData();
                data = compress.unCompress(data);
                dataPackage.data(data);
            }
            out.add(dataPackage);
        } catch (Exception e) {
            LOGGER.error("unCompress err:", e);
            throw new Exception("unCompress err");
        }

    }

}
