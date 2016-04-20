package com.dempe.chat.connector;

import com.dempe.chat.common.BootServer;
import io.netty.channel.ChannelInitializer;

import java.io.IOException;

/**
 * 负责处理连接层的server
 * 关注与client的连接session维护，业务逻辑透传logic层处理，保证服务轻量
 * User: Dempe
 * Date: 2016/4/11
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class ConnectorServer {

    private BootServer server;

    private ChannelInitializer channelInitializer;

    public ChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public void start() throws IOException {
        if (channelInitializer == null) {
            channelInitializer = new ServerChannelInitializer();
        }
        server = new BootServer(channelInitializer);
        server.start();
    }

    public static void main(String[] args) throws IOException {
        new ConnectorServer().start();
    }

}
