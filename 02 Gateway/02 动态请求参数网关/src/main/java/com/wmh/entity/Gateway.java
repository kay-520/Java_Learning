package com.wmh.entity;

import lombok.Data;

/**
 * @author: create by wangmh
 * @name: Gateway.java
 * @description:
 * @date:2020/3/6
 **/
@Data
public class Gateway {
    private int id;
    private String routeId;
    private String routeName;
    private String routePattern;
    private String routeType;
    private String routeUrl;
}
