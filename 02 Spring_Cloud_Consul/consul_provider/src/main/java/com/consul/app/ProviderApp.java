package com.consul.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: App.java
 * @description:
 * @date:2019/11/22
 **/
@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class ProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class, args);
    }

    //url和配置文件中的spring.cloud.consul.discovery.health-check-path值相同
    //必须存在，否者consul服务端 check保存，会导致Fegin调用失败
    @GetMapping("/actuator/health")
    public String health() {
        return "success";
    }

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello";
    }
}
