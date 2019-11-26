package com.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: create by wangmh
 * @name: DiscoveryclientController.java
 * @description:
 * @date:2019/11/26
 **/
@RestController
public class DiscoveryclientController {

    @Autowired// ConsulDiscoveryClient 会在程序启东时，初始化DiscoveryClient实例
    private DiscoveryClient discoveryClient;

    /***
     * 获取服务方信息
     * @param serviceId
     * @return
     *  localhost:8081/getService?serviceId=consul-provider
     */
    @GetMapping("/getService")
    public List<ServiceInstance> getServer(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }
}
