package com.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: create by wangmh
 * @name: RabbitMQConfig.java
 * @description: RabbitMQ配置类
 * @date:2019/12/15
 **/
@Component
public class RabbitMQConfig {
    /**
     * 定义交换机
     */
    private String EXCHANGE_SPRINGBOOT_NAME = "springboot_exchange";


    /**
     * 短信队列
     */
    private String FANOUT_SMS_QUEUE = "fanout_sms_queue";

    /**
     * 邮件队列
     */
    private String FANOUT_EMAIL_QUEUE = "fanout_email_queue";

    /***
     * 创建短信队列
     * @return
     */
    @Bean
    public Queue smsQueue() {
        return new Queue(FANOUT_SMS_QUEUE);
    }

    /***
     * 创建邮件队列
     * @return
     */
    @Bean
    public Queue emailQueue(){
        return new Queue(FANOUT_EMAIL_QUEUE);
    }


    /***
     * 创建交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE_SPRINGBOOT_NAME);
    }

    /***
     * 定义短信队列绑定交换机
     * @param smsQueue
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding smsBindingExchange(Queue smsQueue,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(smsQueue).to(fanoutExchange);
    }

    /***
     * 定义邮件队列绑定交换机
     * @param emailQueue
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding emailBindingExchange(Queue emailQueue,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(emailQueue).to(fanoutExchange);
    }


}
