package com.dempe.ocean.core.frame;

import java.io.IOException;

public interface Server {

    /**
     * 启动服务器
     */
    public void start() throws IOException;

    /**
     * 停止服务器
     */
    public void stop() throws IOException;
}
