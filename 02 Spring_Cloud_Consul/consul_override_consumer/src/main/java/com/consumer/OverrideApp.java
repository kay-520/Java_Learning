package com.consumer;

import com.consumer.feign.Hello1Service;
import com.consumer.feign.Hello2Service;
import com.consumer.my.MyConsulDiscoveryClient;
import com.consumer.my.MyConsulServerList;
import com.ecwid.consul.v1.ConsulClient;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
@EnableHystrix
@EnableCircuitBreaker
@EnableFeignClients
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



    // 使用自定义的 ConsulServerList,
    // 这里的 config 没有使用注入的方式, 因为启动时会报没有 IClientConfig Bean 的错误
    @Bean
    public ServerList<?> ribbonServerList(ConsulClient client, ConsulDiscoveryProperties properties) {
        MyConsulServerList serverList = new MyConsulServerList(client, properties);
        IClientConfig config = new DefaultClientConfigImpl();
        serverList.initWithNiwsConfig(config);
        return serverList;
    }

    @Autowired
    private Hello1Service hello1Service; // consul-provider1 服务提供方
    @Autowired
    private Hello2Service hello2Service; // consul-provider2 服务提供方

    /**
     * 接收前端传来的参数，使用 feign 的方式调用 consul-provider1 远程接口，并返回调用结果
     */
    @GetMapping("/hello1")
    public String hello1() {
        return hello1Service.sayHello();
    }

    /**
     * 接收前端传来的参数，使用 feign 的方式调用 consul-provider2 远程接口，并返回调用结果
     */
    @GetMapping("/hello2")
    public String hello2() {
        return hello2Service.sayHello();
    }
}
