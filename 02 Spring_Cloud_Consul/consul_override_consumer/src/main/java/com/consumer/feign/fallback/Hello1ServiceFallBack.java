package com.consumer.feign.fallback;

import com.consumer.feign.Hello1Service;
import org.springframework.stereotype.Component;

/**
 * @author: create by wangmh
 * @name: Hello1ServiceFallBack.java
 * @description:
 * @date:2019/11/26
 **/
@Component
public class Hello1ServiceFallBack implements Hello1Service {
    @Override
    public String sayHello() {
        return "Hello1ServiceFallBack hystrix!!!";
    }
}
