package com.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author: Wangmh
 * @date: 2019/4/11
 * @time: 14:43
 */
public class MemberServer {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("provider.xml");
        context.start();
        System.out.println("服务启动成功.......");
        System.in.read();
    }
}
