package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @projectName: test
 * @packageName: com
 * @description:
 * @date: 2020/12/4
 **/
@SpringBootApplication
@RestController
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @GetMapping("/")
    public String index() {
        return "hello docker build success";
    }
}
