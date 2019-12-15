package com.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeadLetterMQConfig {
    /**
     * 订单交换机
     */
    @Value("${order.exchange}")
    private String orderExchange;

    /**
     * 订单队列
     */
    @Value("${order.queue}")
    private String orderQueue;

    /**
     * 订单路由key
     */
    @Value("${order.routingKey}")
    private String orderRoutingKey;
    /**
     * 死信交换机
     */
    @Value("${dlx.exchange}")
    private String dlxExchange;

    /**
     * 死信队列
     */
    @Value("${dlx.queue}")
    private String dlxQueue;
    /**
     * 死信路由
     */
    @Value("${dlx.routingKey}")
    private String dlxRoutingKey;

    /**
     * 声明死信交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    /**
     * 声明死信队列
     *
     * @return Queue
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueue);
    }

    /**
     * 声明订单业务交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(orderExchange);
    }

    /**
     * 绑定死信队列到死信交换机
     *
     * @return Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }

    /**
     * 声明订单队列
     *
     * @return Queue
     */
    @Bean
    public Queue orderQueue() {
        // 订单队列绑定我们的死信交换机
        Map<String, Object> arguments = new HashMap<>(2);

        arguments.put("x-dead-letter-exchange", dlxExchange);
        arguments.put("x-dead-letter-routing-key", dlxRoutingKey);
        return new Queue(orderQueue, true, false, false, arguments);
    }

    /**
     * 绑定订单队列到订单交换机
     *
     * @return Binding
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(orderRoutingKey);
    }
}

