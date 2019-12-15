package com.mq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderDlxConsumer {

    /**
     * 死信队列监听队列回调的方法
     *
     * @param msg
     */
    @RabbitListener(queues = "order_dlx_queue")
    public void orderConsumer(String msg) {
        System.out.println("死信队列消费订单消息" + msg);
    }
}
