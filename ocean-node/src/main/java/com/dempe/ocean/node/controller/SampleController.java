package com.dempe.ocean.node.controller;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ocean.common.anno.Param;
import com.dempe.ocean.common.anno.Path;
import com.dempe.ocean.node.service.SampleService;
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

    @Resource
    SampleService lampService;

    /**
     * 默认匹配path getUri=/sample/hello
     * 默认注入request name属性的参数值
     *
     * @return
     */
    @Path
    public JSONObject hello() {
        String name = "hello";
        System.out.println("----------------------------------");
        return lampService.hello(name);
    }
}
