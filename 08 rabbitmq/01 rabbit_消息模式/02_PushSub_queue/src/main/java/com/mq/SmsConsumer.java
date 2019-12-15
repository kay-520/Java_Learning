package com.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: SmsConsumer.java
 * @description: 短信消费者
 * @date:2019/12/6
 **/
public class SmsConsumer {
    /**
     * 定义短信队列
     */
    private static final String QUEUE_NAME = "consumerFanout_sms";
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("短信消费者...");
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        final Channel channel = connection.createChannel();
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("短信消费消息msg:" + msg);
            }
        };
        //3.监听队列 true为自动获取 false手动应答
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
