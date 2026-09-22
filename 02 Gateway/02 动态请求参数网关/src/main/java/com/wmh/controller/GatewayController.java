package com.wmh.controller;

import com.wmh.service.DynamicGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: GatewayController.java
 * @description:
 * @date:2020/3/6
 **/
@RestController
public class GatewayController {

    @Autowired
    private DynamicGateway dynamicGateway;

    @GetMapping("getGatewayConfig")
    public String sysGatewayConfig() {
        dynamicGateway.initRoute();
        return "加载网关配置成功！";
    }
}
