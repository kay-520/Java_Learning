package mq;

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
public class ProducerTopic {
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("生产者启动成功..");
        Connection connection = RabitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        //通道关联交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        String msg = "生产者发送消息内容" + System.currentTimeMillis();
        channel.basicPublish(EXCHANGE_NAME, "wmh.email", null, msg.getBytes());//参数2为路由键
        channel.close();
        connection.close();
    }

}
