package com.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
    @RequestMapping("/error/403")
    public String error403() {
        return "您当前访问的接口权限不足.";
    }
}
