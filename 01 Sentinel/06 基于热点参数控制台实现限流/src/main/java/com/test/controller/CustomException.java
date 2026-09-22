package com.test.controller;

import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: create by wangmh
 * @name: CustomException.java
 * @description: 自定义 热点参数限流 异常处理
 * @date:2020/2/12
 **/
@ControllerAdvice()
public class CustomException {

    @ExceptionHandler(ParamFlowException.class)
    @ResponseBody
    public String getOrderException(){
        return "该用户服务已经限流！";
    }
}
