package com.wmh.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: create by wangmh
 * @projectName: 09 Spring Boot + Redis实现接口幂等性
 * @packageName: com.wmh.service
 * @description:
 * @date: 2020/11/16
 **/
public interface TokenService {
    /**
     * 创建token
     *
     * @return
     */
    String createToken();

    /**
     * 检验token
     *
     * @param request
     * @return
     */
    boolean checkToken(HttpServletRequest request) throws Exception;
}
