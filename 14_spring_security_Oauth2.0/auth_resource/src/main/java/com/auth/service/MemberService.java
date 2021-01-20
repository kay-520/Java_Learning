package com.auth.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberService {
    @GetMapping("/getMember")
    public String getMember() {
        return "我是getMember接口";
    }
}
