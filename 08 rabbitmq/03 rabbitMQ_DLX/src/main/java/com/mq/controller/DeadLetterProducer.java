package com.mq.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeadLetterProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 订单交换机
     */
    @Value("${order.exchange}")
    private String orderExchange;
    /**
     * 订单路由key
     */
    @Value("${order.routingKey}")
    private String orderRoutingKey;

    @RequestMapping("/sendOrder")
    public String sendOrder() {
        String msg = "发送订单测试消息";
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        return "succcess";
    }
}
