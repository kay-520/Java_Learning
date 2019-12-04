package com.consumer.feign;

import com.consumer.feign.fallback.Hello2ServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: create by wangmh
 * @name: Hello1Service.java
 * @description:
 * @date:2019/11/26
 **/
@FeignClient(name = "consul-provider2",fallback = Hello2ServiceFallBack.class)
public interface Hello2Service {
    @GetMapping("/sayHello")
    String sayHello();
}
