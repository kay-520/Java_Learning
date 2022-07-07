package com.test.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: create by wangmh
 * @name: IndexController.java
 * @description: 手动配置管理Api限流接口
 * @date:2020/2/6
 **/
@RestController
public class IndexController {
    private static final String GETORDER_KEY = "getOrder";

    /***
     * 初始化限流配置
     * @return
     */
    @RequestMapping("/initFlowQpsRule")
    public String initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(GETORDER_KEY);
        // QPS控制在2以内
        rule1.setCount(1);
        // QPS限流
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
        return "....限流配置初始化成功..";
    }

    @RequestMapping("/getOrder")
    public String getOrders() {
        Entry entry = null;
        try {
            entry = SphU.entry(GETORDER_KEY);
            // 执行我们服务需要保护的业务逻辑
            return "getOrder接口";
        } catch (Exception e) {
            e.printStackTrace();
            return "该服务接口已经达到上线!";
        } finally {
            // SphU.entry(xxx) 需要与 entry.exit() 成对出现,否则会导致调用链记录异常
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
