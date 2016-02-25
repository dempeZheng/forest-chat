package com.dempe.ocean.core;

import com.dempe.ocean.common.OceanConfig;
import com.dempe.ocean.common.protocol.mqtt.PublishMessage;
import com.dempe.ocean.core.interception.InterceptHandler;
import com.dempe.ocean.core.spi.security.IAuthenticator;
import com.dempe.ocean.core.spi.security.IAuthorizator;
import com.dempe.ocean.core.spi.security.ISslContextCreator;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/25
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private NettyAcceptor m_acceptor;

    private volatile boolean m_initialized;

    private ProtocolProcessor m_processor;

    public static void main(String[] args) throws IOException {
        final Server server = new Server();
        server.startServer();
        System.out.println("Server started, version 0.9-SNAPSHOT");
        //Bind  a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.stopServer();
            }
        });
    }

    /**
     * Starts Moquette bringing the configuration from the file
     * located at m_config/moquette.conf
     */
    public void startServer() throws IOException {
        final OceanConfig config = ConfigFactory.create(OceanConfig.class);
        startServer(config);
    }

    /**
     * Starts Moquette bringing the configuration from the given file
     */
    public void startServer(OceanConfig config) throws IOException {

        startServer(config, null);
    }


    /**
     * Starts Moquette with config provided by an implementation of IConfig class and with the
     * set of InterceptHandler.
     */
    public void startServer(OceanConfig config, List<? extends InterceptHandler> handlers) throws IOException {
        startServer(config, handlers, null, null, null);
    }

    public void startServer(OceanConfig config, List<? extends InterceptHandler> handlers,
                            ISslContextCreator sslCtxCreator, IAuthenticator authenticator,
                            IAuthorizator authorizator) throws IOException {
        if (handlers == null) {
            handlers = Collections.emptyList();
        }


        final ProtocolProcessor processor = SimpleMessaging.getInstance().init(config, handlers, authenticator, authorizator);

        if (sslCtxCreator == null) {
            sslCtxCreator = new DefaultMoquetteSslContextCreator(config);
        }

        m_acceptor = new NettyAcceptor();
        m_acceptor.initialize(processor, config, sslCtxCreator);
        m_processor = processor;
        m_initialized = true;
    }

    /**
     * Use the broker to publish a message. It's intended for embedding applications.
     * It can be used only after the server is correctly started with startServer.
     *
     * @param msg the message to forward.
     * @throws IllegalStateException if the server is not yet started
     */
    public void internalPublish(PublishMessage msg) {
        if (!m_initialized) {
            throw new IllegalStateException("Can't publish on a server is not yet started");
        }
        m_processor.internalPublish(msg);
    }

    public void stopServer() {
        LOG.info("Server stopping...");
        m_acceptor.close();
        SimpleMessaging.getInstance().shutdown();
        m_initialized = false;
        LOG.info("Server stopped");
    }
}
