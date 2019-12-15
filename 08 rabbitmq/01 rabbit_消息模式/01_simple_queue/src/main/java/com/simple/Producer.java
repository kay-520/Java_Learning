package com.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: Producer.java
 * @description: 生产者
 * @date:2019/12/4
 **/
public class Producer {
    private static final String QUEUE_NAME = "karma";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        Channel channel = connection.createChannel();
        //3.设置消息
        String msg = "RabbitMq 简单队列测试消息！！！";
        System.out.println("msg:" + msg);
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        channel.close();
        connection.close();
    }
}
