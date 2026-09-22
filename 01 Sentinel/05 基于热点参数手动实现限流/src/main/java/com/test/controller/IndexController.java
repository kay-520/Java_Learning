package com.test.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: IndexController.java
 * @description: 05 基于热点参数手动实现限流
 * @date:2020/2/6
 **/
@RestController
public class IndexController {
    private static final String GETORDER_KEY = "getOrder";

    @GetMapping("getOrder")
    public String getOrder(int id){
        Entry entry = null;
        try {
            //参数1：资源名称，参数三：计数，参数四：限流参数
            entry = SphU.entry(GETORDER_KEY, EntryType.IN, 1, id);
            // Your logic here.
        } catch (BlockException ex) {
            return "该用户服务已经限流，id="+id;
        }finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return GETORDER_KEY+",id="+id;
    }
}
