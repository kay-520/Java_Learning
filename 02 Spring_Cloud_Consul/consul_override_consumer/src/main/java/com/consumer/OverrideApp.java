package com.consumer;

import com.consumer.my.MyConsulDiscoveryClient;
import com.ecwid.consul.v1.ConsulClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: create by wangmh
 * @name: OverrideApp.java
 * @description:
 * @date:2019/11/26
 **/
@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class OverrideApp {
    public static void main(String[] args) {
        SpringApplication.run(OverrideApp.class, args);
    }

    @Autowired//ConsulDiscoveryClient 会在程序启东时，初始化DiscoveryClient实例
    private MyConsulDiscoveryClient myConsulDiscoveryClient;

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)//保证优先被加载
//    public MyConsulDiscoveryClient consulDiscoveryClient(ConsulClient client, ConsulDiscoveryProperties properties) {
//        return new MyConsulDiscoveryClient(client, properties);
//    }

    // 这里只举例获取服务方信息,不去请求服务方接口
    @GetMapping("/getServer")
    public List<ServiceInstance> getServer(String serviceId) {
        return myConsulDiscoveryClient.getInstances(serviceId);
    }

    @GetMapping("/actuator/health")
    public String health() {
        return "SUCCESS";
    }
}
