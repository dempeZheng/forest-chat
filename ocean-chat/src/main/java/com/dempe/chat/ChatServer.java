package com.dempe.chat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/27
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class ChatServer {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:motan_demo_client.xml"});
    }

}
