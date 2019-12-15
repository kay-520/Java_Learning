package com.mq.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: FanoutProducerCpntroller.java
 * @description:
 * @date:2019/12/15
 **/
@RestController
public class FanoutProducerController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /***
     * 向rabbitmq中发送消息（发布订阅模式）
     * @param msg
     * @return
     */
    @RequestMapping("/sendMsg")
    public String sendMsg(String msg) {
        //参数1 交换机名称 、参数2路由key  参数3 消息
        amqpTemplate.convertAndSend("springboot_exchange", "", msg);
        return "success";
    }

    @RequestMapping("/sendMsg2")
    public String sendMsg(Integer msg) {
        amqpTemplate.convertAndSend("springboot_exchange", "", msg);
        return "success";
    }


    /***
     * 接收短信消息
     * @return
     */
    @RequestMapping("/receiveMsg2")
    public String receiveMsg() {
        String fanout_sms_queue = amqpTemplate.receiveAndConvert("fanout_sms_queue").toString();
        return "接收到消息："+fanout_sms_queue;
    }
}
