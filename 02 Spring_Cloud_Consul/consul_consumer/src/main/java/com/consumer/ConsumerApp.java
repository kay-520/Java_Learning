package com.consumer;

import com.consumer.fegin.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author: create by wangmh
 * @name: App.java
 * @description:
 * @date:2019/11/25
 **/
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableCircuitBreaker
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }

    @Autowired
    private IndexService indexService;

    @GetMapping("/index")
    public String index(@RequestParam String param) {
        return indexService.index(param);
    }

    @GetMapping("/sayHello")
    public String sayHello(){
        return indexService.sayHello();
    }

    //url和配置文件中的spring.cloud.consul.discovery.health-check-path值相同 (自定义安全检查)
    //如果自定义了健康检查，就必须定义该接口，否者consul服务端 check保存，会导致Fegin调用失败
    @GetMapping("/actuator/health")
    public String health() {
        return "success";
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取服务信息
     */
    @RequestMapping("/services")
    public Object services() {
        return discoveryClient.getInstances("consul-provider");
    }
}
