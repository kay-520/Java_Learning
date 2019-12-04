package com.plus.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.plus.pojo.User;
import com.plus.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wmh
 * @since 2019-03-08
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/show")
    public JSONObject test(){
        Page<User> users=iUserService.selectPage(new Page<>(1,10));

        JSONObject object=new JSONObject();
        object.put("users",users);
        return object;
    }

}
