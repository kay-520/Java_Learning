package com.test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: IndexController.java
 * @description: 03 基于Nacos实现数据持久化
 * @date:2020/2/6
 **/
@RestController
public class IndexController {

    /***
     * 基于平均响应时间
     * @return
     */
    @SentinelResource(value = "getERFallBack",fallback = "getException")
    @GetMapping("getERFallBack")
    public String getERFallBack(){
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "getERFallBack";
    }

    /***
     * 基于异常比例
     * @param i
     * @return
     */
    @SentinelResource(value = "getErroPer",fallback = "getException")
    @GetMapping("/getErroPer")
    public String getErroPer(int i){
        int j=1/i;
        return "getErroPer";
    }

    /***
     * 基于异常次数
     * @param i
     * @return
     */
    @SentinelResource(value = "getErroCout",fallback = "getException")
    @GetMapping("/getErroCout")
    public String getErroCout(int i){
        int j=1/i;
        return "getErroCout";
    }

    /***
     * 参数必须与被降级的方法中的参数一致，才能实现降级
     * 异常比例和异常次数 的降级方法
     * @param i
     * @return
     */
    public String getException(int i){
        return "服务降级啦，当前服务器请求次数过多，请稍后重试!";
    }

    /***
     * 基于平均响应时间 的降级方法
     * @return
     */
    public String getException(){
        return "服务降级啦，当前服务器请求次数过多，请稍后重试!";
    }


}
