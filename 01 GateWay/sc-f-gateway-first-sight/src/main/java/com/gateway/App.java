package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author: create by wangmh
 * @projectName: scfgatewayfirstsight
 * @packageName: com.gateway
 * @description:
 * @date: 2019/10/31
 **/
@SpringBootApplication
@RestController
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        String httpUri = "http://httpbin.org:80";
        return builder.routes()
				//当访问/get请求，并携带Hello请求头，可进入http://httpbin.org:80
                .route(p -> p.path("/get")
                        .filters(f ->
                                f.addRequestHeader("Hello", "World"))
                        .uri(httpUri))
                .route(p->p.host("*.hystrix.com")
                        .filters(f->
                                f.hystrix(config -> config
                                        .setName("mycmd")
                                        .setFallbackUri("forward:/fallback")))
                        .uri(httpUri))
                .build();
    }

    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
}
