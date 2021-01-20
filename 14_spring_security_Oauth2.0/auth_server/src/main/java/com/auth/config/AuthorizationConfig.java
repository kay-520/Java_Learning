package com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author: create by wangmh
 * @projectName: 14_spring_security_Oauth2.0
 * @packageName: com.auth.config
 * @description: 认证授权中心
 * @date: 2021/1/20
 **/
@Component
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单提交 检查accessToken是否有效期的情况下
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()");
    }

    /**
     * 分配我们的appid和appkey
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 读数据库
        clients.inMemory()
                // appid
                .withClient("test_appId")
                .secret(passwordEncoder.encode("test_appkey"))
                // 授权码
                .authorizedGrantTypes("authorization_code", "refresh_token")
                // 作用域 表示所有的接口都可以访问 分配我们的appid 调用接口的权限
                .scopes("all")
                .resourceIds("auth_resource")
                // 用户选择授权之后，跳转到该地址传递code授权码
                .redirectUris("http://karma520.com/callback");


    }

}
