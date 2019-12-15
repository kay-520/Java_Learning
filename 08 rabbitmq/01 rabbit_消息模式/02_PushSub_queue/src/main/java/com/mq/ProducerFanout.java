package com.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: ProducerFanout.java
 * @description: 生产者
 * @date:2019/12/6
 **/
public class ProducerFanout {
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        //通道关联交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
        String msg = "发布订阅测试消息！！！！！！！";
        channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
        channel.close();
        connection.close();
    }

}
