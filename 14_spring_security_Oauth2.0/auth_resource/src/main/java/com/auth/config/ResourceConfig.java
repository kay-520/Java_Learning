package com.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author: create by wangmh
 * @projectName: 14_spring_security_Oauth2.0
 * @packageName: com.auth.config
 * @description: 资源服务端
 * @date: 2021/1/20
 **/
@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {
    @Value("${auth.appid}")
    private String appId;
    @Value("${auth.appsecret}")
    private String appsecret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Primary
    @Bean
    public RemoteTokenServices remoteTokenServices() {
        final RemoteTokenServices tokenServices = new RemoteTokenServices();
        //设置授权服务器check_token端点完整地址
        tokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        //设置客户端id与secret，注意：client_secret值不能使用passwordEncoder加密！
        tokenServices.setClientId(appId);
        tokenServices.setClientSecret(appsecret);
        return tokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //设置创建session策略
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        //@formatter:off
        //所有请求必须授权
        http.authorizeRequests()
                .anyRequest().authenticated();
        //@formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //resourceId资源iD
        resources.resourceId("auth_resource").stateless(true);
    }

}
