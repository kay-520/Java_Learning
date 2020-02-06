package com.test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author: create by wangmh
 * @name: IndexController.java
 * @description: 注解形式配置管理Api限流
 * @date:2020/2/6
 **/
@RestController
public class IndexController {
    private static final String GETORDER_KEY = "getOrder";

    /***
     * @SentinelResource 流量规则资源名
     * - blockHandler 限流/熔断 出现异常 执行的方法
     * - fallback 服务降级执行的方法
     * -QPS=并发数/平均响应时间
     * @return
     */
    @SentinelResource(value = GETORDER_KEY,blockHandler = "getOrderQpsException")
    @RequestMapping("/getOrder")
    public String getOrders() {
        return "getOrder接口";
    }

    /**
     * 被限流后返回的提示
     *
     * @param e
     * @return
     */
    public String getOrderQpsException(BlockException e) {
        e.printStackTrace();
        return "该接口已经被限流啦!";
    }

    /***
     * 基于并发数量处理限流
     * 并发数 = QPS*平均响应时间
     * 每次最多只会有一个线程处理该业务逻辑，超出该阈值的情况下，直接拒绝访问。
     * @return
     */
    @SentinelResource(value = "getOrderThrad", blockHandler = "getOrderQpsException")
    @RequestMapping("/getOrderThrad")
    public String getgetOrderThread(){
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "getOrderThrad";
    }
}
