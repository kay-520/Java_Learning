package com.test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: IndexController.java
 * @description: 06 基于热点参数控制台实现限流
 * @date:2020/2/6
 **/
@RestController
public class IndexController {
    private static final String GETORDER_KEY = "getOrder";

    @GetMapping("getOrder")
    @SentinelResource(GETORDER_KEY)
    public String getOrder(int id) {
        return GETORDER_KEY + ".id=" + id;
    }
}
