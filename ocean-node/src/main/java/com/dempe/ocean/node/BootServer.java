package com.dempe.ocean.node;


import com.dempe.ocean.common.OceanConfig;
import com.dempe.ocean.common.register.ForestNameService;
import com.dempe.ocean.core.frame.ServerContext;
import io.netty.bootstrap.ServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * 框架启动基类
 * User: Dempe
 * Date: 2015/10/15
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */

public class BootServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootServer.class);

    ApplicationContext context;

    private OceanConfig config;
    private ServerContext servercontext;
    private ForestNameService forestNameService;
    private NodeServerAcceptor acceptor;


    public BootServer(OceanConfig config, ApplicationContext context) {
        this.config = config;
        this.context = context;
        servercontext = new ServerContext(config, context);
        acceptor = new NodeServerAcceptor(new ProcessorHandler(servercontext));

    }


    public void start() throws IOException {
        acceptor.initialize(config);
    }

    public BootServer registerNameService() throws Exception {
        forestNameService = new ForestNameService();
        forestNameService.start();
        forestNameService.register();
        return this;
    }


}
