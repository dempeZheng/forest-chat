package com.dempe.ocean.common.pack;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public interface Marshallable {

    /**
     * 消息编码类
     *
     * @param pack
     */
    Pack marshal(Pack pack);

    /**
     * 消息编码类
     *
     * @param unpack
     */
    Marshallable unmarshal(Unpack unpack) throws IOException;
}