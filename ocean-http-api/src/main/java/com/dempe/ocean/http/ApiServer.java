package com.dempe.ocean.http;

import com.google.common.collect.Sets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

/**
 * 基于spring boot & motan的http api server 启动类
 * User: Dempe
 * Date: 2016/9/9
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
@SpringBootApplication
public class APIServer {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(APIServer.class);
        Set<Object> set = Sets.newHashSet();
        set.add("classpath*:api_motan_client.xml");
        app.setSources(set);
        app.run(args);
    }


}
