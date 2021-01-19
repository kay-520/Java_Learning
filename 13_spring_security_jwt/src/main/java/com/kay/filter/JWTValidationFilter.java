package com.kay.filter;

import com.kay.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author: create by wangmh
 * @projectName: 13_spring_security_jwt
 * @packageName: com.kay.filter
 * @description: 请求拦截token验证
 * @date: 2021/1/19
 **/
public class JWTValidationFilter  extends BasicAuthenticationFilter {
    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 过滤请求验证
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(setAuthentication(request.getHeader("token")));
        super.doFilterInternal(request, response, chain);
    }


    /**
     * 验证token 并且验证权限
     * @param token
     * @return
     */
    private UsernamePasswordAuthenticationToken setAuthentication(String token) {
        String username = JwtUtils.getUsername(token);
        if (username == null) {
            return null;
        }
        List<SimpleGrantedAuthority> userRoleList = JwtUtils.getUserRole(token);
        return new UsernamePasswordAuthenticationToken(username, null, userRoleList);
    }

}
