package com.wmh.service;

import com.wmh.entity.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: create by wangmh
 * @name: DynamicGateway.java
 * @description: 配置动态路由服务
 * @date:2020/3/6
 **/
@Service
public class DynamicGateway implements ApplicationEventPublisherAware {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private GatewayService gatewayService;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


    public void initRoute() {
        List<Gateway> gatewayList = gatewayService.list();
        gatewayList.forEach(item -> {
            loadRoute(item);
        });
    }

    public String loadRoute(Gateway gateway) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicate = new PredicateDefinition();
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        // 如果配置路由type为0的话 则从注册中心获取服务
        URI uri = null;
        if (gateway.getRouteType().equals("0")) {
            uri = UriComponentsBuilder.fromUriString("lb://" + gateway.getRouteUrl() + "/").build().toUri();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(gateway.getRouteUrl()).build().toUri();
        }
        // 定义的路由唯一的id
        definition.setId(gateway.getRouteId());
        predicate.setName("Path");
        //路由转发地址
        predicateParams.put("pattern", gateway.getRoutePattern());
        predicate.setArgs(predicateParams);

        // 名称是固定的, 路径去前缀
        filterDefinition.setName("StripPrefix");
        filterParams.put("_genkey_0", "1");
        filterDefinition.setArgs(filterParams);
        definition.setPredicates(Arrays.asList(predicate));
        definition.setFilters(Arrays.asList(filterDefinition));
        definition.setUri(uri);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }
}
