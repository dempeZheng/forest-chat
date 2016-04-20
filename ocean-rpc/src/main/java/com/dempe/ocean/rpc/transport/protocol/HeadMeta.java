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

package com.dempe.ocean.rpc.transport.protocol;

import java.lang.*;
import java.nio.ByteBuffer;


/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class HeadMeta implements Writerable, Readable {

    /**
     * RPC meta head size
     */
    public static final int SIZE = 12;

    /**
     * 协议标识
     */
    private byte[] sign;

    /**
     * message body size include
     */
    private int msgSize;

    /**
     * RPC meta size
     */
    private int metaSize;

    /**
     * get the magicCode
     *
     * @return the magicCode
     */
    public byte[] getSign() {
        return sign;
    }

    /**
     * @return the mgcode of string
     */
    public String getSignAsString() {
        if (sign == null) {
            return null;
        }
        return new String(sign, ProtocolConstant.CHARSET);
    }

    /**
     * set magicCode value to magicCode
     *
     * @param sign magic code string
     */
    public void setMagicCode(String sign) {
        if (sign == null) {
            throw new IllegalArgumentException("invalid magic code. size must be 4.");
        }
        setMagicCode(sign.getBytes(ProtocolConstant.CHARSET));
    }

    /**
     * set magicCode value to magicCode
     *
     * @param sign the magicCode to set
     */
    public void setMagicCode(byte[] sign) {
        if (sign == null || sign.length != 4) {
            throw new IllegalArgumentException("invalid magic code. size must be 4.");
        }
        this.sign = sign;
    }

    /**
     * get the msgSize
     *
     * @return the msgSize
     */
    public int getMsgSize() {
        return msgSize;
    }

    /**
     * set msgSize value to msgSize
     *
     * @param msgSize the msgSize to set
     */
    public void setMsgSize(int msgSize) {
        this.msgSize = msgSize;
    }

    /**
     * get the metaSize
     *
     * @return the metaSize
     */
    public int getMetaSize() {
        return metaSize;
    }

    /**
     * set metaSize value to metaSize
     *
     * @param metaSize the metaSize to set
     */
    public void setMetaSize(int metaSize) {
        this.metaSize = metaSize;
    }

    public byte[] write() {
        ByteBuffer allocate = ByteBuffer.allocate(SIZE);
        allocate.put(sign);
        allocate.putInt(msgSize);
        allocate.putInt(metaSize);
        byte[] ret = allocate.array();
        allocate.clear();
        return ret;
    }


    public void read(byte[] bytes) {
        if (bytes == null || bytes.length != SIZE) {
            throw new IllegalArgumentException("invalid byte array. size must be " + SIZE);
        }

        ByteBuffer allocate = ByteBuffer.wrap(bytes);
        sign = new byte[4]; // magic code size must be 4.
        allocate.get(sign);
        msgSize = allocate.getInt();
        metaSize = allocate.getInt();

        allocate.clear();
    }

    public HeadMeta copy() {
        HeadMeta rpcHeadMeta = new HeadMeta();
        rpcHeadMeta.setMagicCode(getSign());
        rpcHeadMeta.setMsgSize(getMsgSize());
        rpcHeadMeta.setMetaSize(getMetaSize());
        return rpcHeadMeta;
    }

}
