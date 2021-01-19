package com.kay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: create by wangmh
 * @projectName: 13_spring_security_jwt
 * @packageName: com.kay
 * @description:
 * @date: 2021/1/19
 **/
@MapperScan("com.kay.mapper")
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
