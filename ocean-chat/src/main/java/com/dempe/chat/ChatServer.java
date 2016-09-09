package com.dempe.chat;

import com.dempe.chat.connector.ConnectorServer;
import com.dempe.chat.connector.MQTTHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/27
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class ChatServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChatServer.class);


    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:app*.xml"});
        LOGGER.info("app context init");
        MQTTHandler mqttHandler = ctx.getBean(MQTTHandler.class);
        LOGGER.info("app context init");
        new ConnectorServer(mqttHandler).start();
    }

}
