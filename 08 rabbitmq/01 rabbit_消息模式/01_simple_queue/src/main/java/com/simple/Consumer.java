package com.simple;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: Consumer.java
 * @description: 消费者
 * @date:2019/12/4
 **/
public class Consumer {
    private static final String QUEUE_NAME = "karma";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        Channel channel = connection.createChannel();
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("msg:" + msg);
            }
        };
        //3.监听队列 true为自动获取
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
