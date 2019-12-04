package com.consumer.feign.fallback;

import com.consumer.feign.Hello2Service;
import org.springframework.stereotype.Component;

/**
 * @author: create by wangmh
 * @name: Hello2ServiceFallBack.java
 * @description:
 * @date:2019/11/26
 **/
@Component
public class Hello2ServiceFallBack implements Hello2Service {
    @Override
    public String sayHello() {
        return "Hello2ServiceFallBack hystrix!!!!";
    }
}
