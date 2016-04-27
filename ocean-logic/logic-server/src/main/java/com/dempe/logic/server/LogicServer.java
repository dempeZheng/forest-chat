package com.dempe.logic.server;

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


    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:motan_demo_server.xml"});
        System.out.println("server start...");
    }

}
