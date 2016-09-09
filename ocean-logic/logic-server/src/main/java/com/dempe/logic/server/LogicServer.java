package com.dempe.logic.server;

import com.alibaba.fastjson.JSONObject;
import com.dempe.logic.api.UserService;
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
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:app*.xml"});
        UserService userServiceImpl = (UserService) applicationContext.getBean("userServiceImpl");
        System.out.println(userServiceImpl);
        JSONObject login = userServiceImpl.login("uid", "pwd");
        System.out.println(login);
        System.out.println("server start...");
    }

}
