package com.consumer.feign;

import com.consumer.feign.fallback.Hello1ServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: create by wangmh
 * @name: Hello1Service.java
 * @description:
 * @date:2019/11/26
 **/
@FeignClient(name = "consul-provider1",fallback = Hello1ServiceFallBack.class)
public interface Hello1Service {
    @GetMapping("/sayHello")
    String sayHello();
}
