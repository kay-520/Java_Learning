package com.kay.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kay.entity.UserEntity;
import com.kay.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author: create by wangmh
 * @projectName: 13_spring_security_jwt
 * @packageName: com.kay.filter
 * @description: 登陆成功返回token
 * @date: 2021/1/19
 **/
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * 获取授权管理
     */
    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        /**
         *  后端登陆接口
         */
        super.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) {
        try {
            UserEntity user = new ObjectMapper()
                    .readValue(req.getInputStream(), UserEntity.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 用户登陆成功之后验证
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserEntity userEntity = (UserEntity) authResult.getPrincipal();
        String jwtToken = JwtUtils.generateJsonWebToken(userEntity);
        response.addHeader("token", jwtToken);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print("账号或者密码错误");
    }

}
