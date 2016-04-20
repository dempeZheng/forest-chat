package com.dempe.ocean.logic;


import com.dempe.ocean.rpc.AppConfig;
import com.dempe.ocean.rpc.ForestServer;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于spring注解的sampleServer
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class ChatLogicServer {

    public static void main(String[] args) throws Exception {
        // 启动spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        // 生成开发环境的配置
        AppConfig devConfig = ConfigFactory.create(AppConfig.class);
        ForestServer server = new ForestServer(devConfig, context);
        server
//                .registerNameService()
                .start();
    }
}
