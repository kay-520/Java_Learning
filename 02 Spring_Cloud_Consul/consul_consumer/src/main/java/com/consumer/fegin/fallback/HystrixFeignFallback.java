package com.consumer.fegin.fallback;

import com.consumer.fegin.IndexService;
import org.springframework.stereotype.Component;

/**
 * @author: create by wangmh
 * @name: HystrixFeignFallback.java
 * @description:
 * @date:2019/11/25
 **/
@Component
public class HystrixFeignFallback implements IndexService {
    @Override
    public String index(String parma) {
        return "index Hystrix";
    }

    @Override
    public String sayHello() {
        return "sayHello Hystrix";
    }
}
