package com.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: Consumer.java
 * @description: 消费者
 * @date:2019/12/6
 **/
public class Consumer {
    private static final String QUEUE_NAME = "karma";
    private static int serviceTimeOut = 1000;

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        final Channel channel = connection.createChannel();
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("消费消息msg:" + msg);
                //手动ack应答模式 在处理完消息时，返回应答状态，true表示为自动应答模式。
                channel.basicAck(envelope.getDeliveryTag(), false);
                try {
                    Thread.sleep(serviceTimeOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //3.监听队列 true为自动获取 false手动应答
        channel.basicConsume(QUEUE_NAME, false, defaultConsumer);
    }
}
