package com.dempe.ocean.node.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.client.bus.cluster.HABusCliService;
import com.dempe.ocean.common.anno.Path;
import com.dempe.ocean.node.service.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * sample业务开发controller
 * User: Dempe
 * Date: 2016/1/28
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
@Controller("sample")
public class SampleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @Resource
    private SampleService lampService;

    @Resource
    private HABusCliService busCliService;


    /**
     * 默认匹配path getUri=/sample/hello
     * 默认注入request name属性的参数值
     *
     * @return
     */
    @Path
    public JSONObject hello() {
        LOGGER.info("hello method invoked>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        LOGGER.info("busCliService send bc for test>>>busCliService:{}", busCliService);
        String name = "hello";
        return lampService.hello(name);
    }
}
