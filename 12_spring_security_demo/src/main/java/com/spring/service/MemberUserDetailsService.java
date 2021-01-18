package com.spring.service;

import com.spring.entity.PermissionEntity;
import com.spring.entity.UserEntity;
import com.spring.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MemberUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    /**
     * loadUserByUserName
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据该用户名称查询在数据库中是否存在
        UserEntity userEntity = userMapper.findByUsername(username);
        if (userEntity == null) {
            return null;
        }
        // 2.查询对应的用户权限
        List<PermissionEntity> listPermission = userMapper.findPermissionByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        listPermission.forEach(user -> {
            authorities.add(new SimpleGrantedAuthority(user.getPermTag()));
        });
        log.info(">>>authorities:{}<<<", authorities);
        // 3.将该权限添加到security
        userEntity.setAuthorities(authorities);
        return userEntity;
    }
}
