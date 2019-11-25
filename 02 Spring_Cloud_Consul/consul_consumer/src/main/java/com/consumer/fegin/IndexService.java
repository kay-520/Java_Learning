package com.consumer.fegin;

import com.consumer.fegin.fallback.HystrixFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: create by wangmh
 * @name: IndexService.java
 * @description:
 * @date:2019/11/25
 **/
@FeignClient(name = "consul-provider",fallback = HystrixFeignFallback.class)
public interface IndexService {
    @GetMapping("/index")
    String index(@RequestParam(value = "param") String param);

    @GetMapping("/sayHello")
    String sayHello();
}
