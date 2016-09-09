package com.dempe.logic.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/27
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class LogicServer {


    private final static Logger LOGGER = LoggerFactory.getLogger(LogicServer.class);

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:app*.xml"});

        LOGGER.info("server start...");
    }

}
