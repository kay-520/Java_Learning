package com.consul.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: IndexController.java
 * @description:
 * @date:2019/11/25
 **/
@RestController
public class IndexController {
    @GetMapping("/index")
    public String index(@RequestParam("param") String param){
        return param;
    }
}
