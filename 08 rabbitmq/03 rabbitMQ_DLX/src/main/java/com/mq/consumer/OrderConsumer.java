package com.mq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    /**
     * 监听队列回调的方法
     *
     * @param msg
     */
    @RabbitListener(queues = "order_queue")
    public void orderConsumer(String msg) {
        System.out.println("正常订单消费者消息msg:" + msg);
    }
}
