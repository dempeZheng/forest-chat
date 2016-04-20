package com.dempe.chat.connector;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 扩展协议，主要用来优化mqtt对im支持，参见陌陌&微信的处理方法
 * 1.有新的消息后server主动push
 * 2.client收到push消息后，发sync同步消息，
 * 3.server按redis sort set里面版本好发送消息给客户端
 * 4.client返回收到的序列号
 * User: Dempe
 * Date: 2016/4/12
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class ExtendProtocol {

    public ByteBuffer encodeSrvPush() {

        return null;
    }

    public ByteBuffer encodeCliSynReq() {
        return null;
    }

    public ByteBuffer encodeSrvSynRsp(List<Object> list) {
        return null;
    }

    public ByteBuffer encodeSeqRsp(Integer seq) {
        return null;
    }


}
