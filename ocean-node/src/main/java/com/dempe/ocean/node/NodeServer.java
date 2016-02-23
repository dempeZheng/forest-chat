package com.dempe.ocean.node;

import com.dempe.ocean.common.OceanConfig;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 基于spring注解的sampleServer
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan
public class NodeServer {

    public static void main(String[] args) throws Exception {
        // 启动spring容器
        ApplicationContext context = new AnnotationConfigApplicationContext(NodeServer.class);
        // 生成开发环境的配置
        OceanConfig config = ConfigFactory.create(OceanConfig.class);
        BootServer server = new BootServer(config, context);
        server.registerNameService()
                .start();
    }
}
