package com.wmh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: create by wangmh
 * @projectName: 09 Spring Boot + Redis实现接口幂等性
 * @packageName: com.wmh
 * @description:
 * @date: 2020/11/16
 **/
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
