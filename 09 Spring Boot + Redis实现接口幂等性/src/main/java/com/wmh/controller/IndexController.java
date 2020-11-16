package com.wmh.controller;

import com.wmh.service.TokenService;
import com.wmh.annotation.AutoIdempotent;
import com.wmh.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class IndexController {


    @Autowired
    private TokenService tokenService;

    /***
     * 1.创建Token，并获取
     * @return
     */
    @GetMapping("/api/token")
    public Object getToken() {
        String token = tokenService.createToken();
        return ResponseUtil.ok(token);
    }


    /***
     * 2.接口幂等性测试
     * @return
     */
    @AutoIdempotent
    @GetMapping("/api/Idempotence")
    public Object testIdempotence() {
        return ResponseUtil.ok("接口幂等性测试");
    }
}