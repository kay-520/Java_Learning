package com.wmh.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: create by wangmh
 * @projectName: 09 Spring Boot + Redis实现接口幂等性
 * @packageName: com.wmh.interceptor
 * @description:
 * @date: 2020/11/16
 **/
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {

    @Bean
    AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(adminInterceptor());
        registration.addPathPatterns("/**");                      //所有路径都被拦截
//        registration.excludePathPatterns(                         //添加不拦截路径
//                "/api/*"
//        );
        super.addInterceptors(registry);
    }
}
