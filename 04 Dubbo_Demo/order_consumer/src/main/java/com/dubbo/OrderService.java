package com.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 14:48
 */
public class OrderService {
    public static void addOrder(){
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        System.out.println("###order服務,开始调用会员服务");
        UserService userService=(UserService) context.getBean("userService");
        String userName=userService.getUser(1L);
        System.out.println("###order服務,结束调用会员服务,userName:" + userName);
    }

    public static void main(String[] args) {
        addOrder();
    }
}
