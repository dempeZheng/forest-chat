package com.dempe.ocean.rpc;

import com.dempe.ocean.rpc.core.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class ForestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForestServer.class);

    ApplicationContext context;
    private BootServer bootServer;
    private AppConfig config;

    public ForestServer(AppConfig config, ApplicationContext context) {
        this.config = config;
        this.context = context;
        ServerHandlerInitializer channelInitializer = new ServerHandlerInitializer(new ServerContext(config, context));
        bootServer = new BootServer(channelInitializer);
    }


    public void start() throws IOException {
        bootServer.start();
    }

    public void stop() throws IOException {
        bootServer.stop();
    }

    public ForestServer stopWithJVMShutdown() {
        bootServer.stopWithJVMShutdown();
        return this;
    }
}
