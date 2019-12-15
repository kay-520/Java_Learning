package com.mq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: RabitMQConnection.java
 * @description: 获取连接
 * @date:2019/12/6
 **/
public class RabitMQConnection {

    public static Connection getConnection() throws IOException, TimeoutException {
        //1.创建连接
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置连接地址
        factory.setHost("127.0.0.1");
        //3.设置端口
        factory.setPort(5672);
        //4.设置账号密码
        factory.setUsername("karma");
        factory.setPassword("karma");
        //5.设置VirtualHost
        factory.setVirtualHost("/wmh");
        return factory.newConnection();
    }
}
