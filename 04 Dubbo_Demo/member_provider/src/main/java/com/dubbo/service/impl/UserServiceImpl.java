package com.dubbo.service.impl;


import com.dubbo.UserService;

/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 14:36
 */
public class UserServiceImpl implements UserService {
    public String getUser(Long userId) {
        System.out.println("###会员服务接受参数开始userId:" + userId);
        if (userId == 1) {
            return "王明欢";
        }
        if (userId == 2) {
            return "小白白";
        }
        System.out.println("###会员服务接受参数结束###");
        return "未找到用户";
    }
}
