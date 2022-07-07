package com.test.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author: create by wangmh
 * @name: SentinelApplicationRunner.java
 * @description: 手动放入到项目启动自动加载
 * @date:2020/2/6
 **/
@Component
public class SentinelApplicationRunner implements ApplicationRunner {
    private static final String GETORDER_KEY = "getOrder";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 定义热点限流的规则，对第一个参数设置 qps 限流模式，阈值为5
        ParamFlowRule rule = new ParamFlowRule(GETORDER_KEY)
                //热点参数的索引
                .setParamIdx(0)
                //限流模式
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                //阈值
                .setCount(2);
        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }
}
