package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: create by wangmh
 * @projectName: 11_redis_cache_uniformity
 * @packageName: org.example
 * @description:
 * @date: 2020/12/21
 **/
@SpringBootApplication
@MapperScan("org.example.mapper")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
