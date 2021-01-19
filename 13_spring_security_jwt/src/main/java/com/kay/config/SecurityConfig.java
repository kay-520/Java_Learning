package com.kay.config;

import com.kay.entity.PermissionEntity;
import com.kay.filter.JWTLoginFilter;
import com.kay.filter.JWTValidationFilter;
import com.kay.mapper.PermissionMapper;
import com.kay.service.MemberUserDetailsService;
import com.kay.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MemberUserDetailsService memberUserDetailsService;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 添加授权账户
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberUserDetailsService).passwordEncoder(new PasswordEncoder() {
            /**
             * 对密码MD5
             * @param rawPassword
             * @return
             */
            @Override
            public String encode(CharSequence rawPassword) {
                return MD5Util.encode((String) rawPassword);
            }

            /**
             * rawPassword 用户输入的密码
             * encodedPassword 数据库DB的密码
             * @param rawPassword
             * @param encodedPassword
             * @return
             */
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String rawPass = MD5Util.encode((String) rawPassword);
                boolean result = rawPass.equals(encodedPassword);
                return result;
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<PermissionEntity> allPermission = permissionMapper.findAllPermission();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                expressionInterceptUrlRegistry = http.authorizeRequests();
        allPermission.forEach((permission) -> {
            expressionInterceptUrlRegistry.antMatchers(permission.getUrl()).
                    hasAnyAuthority(permission.getPermTag());

        });
        expressionInterceptUrlRegistry.antMatchers("/auth/login").permitAll()
                .antMatchers("/**").fullyAuthenticated()
                .and()
                //添加登录返回jwt信息、登陆验证 filter
                .addFilter(new JWTValidationFilter(authenticationManager()))
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .csrf().disable()
                //剔除session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * There is no PasswordEncoder mapped for the id "null"
     * 原因:升级为Security5.0以上密码支持多中加密方式，恢复以前模式
     *
     * @return
     */
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
