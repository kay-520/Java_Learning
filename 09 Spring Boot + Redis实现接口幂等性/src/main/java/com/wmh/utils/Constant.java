package com.wmh.utils;

/**
 * @author: create by wangmh
 * @projectName: 09 Spring Boot + Redis实现接口幂等性
 * @packageName: com.wmh.utils
 * @description:
 * @date: 2020/11/16
 **/
public interface Constant {
    String TOKEN_PREFIX = "user:token:";
    String ACCESS_TOKEN = "access_token";

    String REPETITIVE_OPERATION = "重复性操作";
}
