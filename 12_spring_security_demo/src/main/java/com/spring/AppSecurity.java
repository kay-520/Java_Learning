package com.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.spring.mapper")
@SpringBootApplication
public class AppSecurity {
    public static void main(String[] args) {
        SpringApplication.run(AppSecurity.class);
    }
}
