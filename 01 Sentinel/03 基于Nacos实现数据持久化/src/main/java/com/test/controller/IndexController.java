package com.test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: IndexController.java
 * @description: 03 基于Nacos实现数据持久化
 * @date:2020/2/6
 **/
@RestController
public class IndexController {

    @SentinelResource(value = "getNacosInfo",blockHandler = "getQpsException")
    @GetMapping("/getNacosInfo")
    public String getNacosInfo(){
        return "getNacosInfo";
    }

    public String getQpsException(){
        return "该接口已被限流，请稍后再试";
    }
}
