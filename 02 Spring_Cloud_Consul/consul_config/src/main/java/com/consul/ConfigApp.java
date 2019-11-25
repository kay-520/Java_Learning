package com.consul;

import com.consul.config.StudentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @name: App.java
 * @description:
 * @date:2019/11/25
 **/
@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableScheduling//必须开启 定时任务
@RefreshScope
@EnableConfigurationProperties({StudentConfig.class})
public class ConfigApp {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApp.class, args);
    }

    //consul中配置目录规则：spring.cloud.consul.config.prefix/spring.cloud.consul.config.name/spring.cloud.consul.config.data-key
    @Value("${foo.bar.name}")
    private String name;

    @GetMapping("/getName")
    public String getName() {
        return name;
    }

    @Autowired
    private StudentConfig studentConfig;

    @GetMapping("/getStu")
    public String getStu() {
        return studentConfig.toString();
    }

    //url和配置文件中的spring.cloud.consul.discovery.health-check-path值相同
    //必须存在，否者consul服务端 check保存，会导致Fegin调用失败
    @GetMapping("/actuator/health")
    public String health() {
        return "success";
    }
}
