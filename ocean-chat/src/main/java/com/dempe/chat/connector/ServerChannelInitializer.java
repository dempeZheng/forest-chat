package com.dempe.chat.connector;

import com.dempe.chat.common.mqtt.codec.MQTTDecoder;
import com.dempe.chat.common.mqtt.codec.MQTTEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        // 解码
        p.addLast("mqttDecoder", new MQTTDecoder());
        // 编码
        p.addLast("mqttEncoder", new MQTTEncoder());

        p.addLast("mqttHandler", new MQTTHandler());
    }

}