package com.mq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: create by wangmh
 * @name: FanoutEmailConsumer.java
 * @description:
 * @date:2019/12/15
 **/
@Component
@RabbitListener(queues = {"fanout_email_queue"})//队列中有消息时则会进行接收并处理
public class FanoutEmailConsumer {

    //@RabbitListener 标注在类上面表示当有收到消息的时候，
    // 就交给 @RabbitHandler 的方法处理，具体使用哪个方法处理，
    // 根据 MessageConverter 转换后的参数类型
    @RabbitHandler
    public void process(String msg) {
        System.out.println("邮件消费者消息String类型msg:" + msg);
    }

    @RabbitHandler
    public void process2(Integer msg) {
        System.out.println("邮件消费者消息Integer类型msg:" + msg);
    }

}
