### RabbitMQ消息幂等问题

#### 一、RabbitMQ消息自动重试机制

> 1. 当消费者业务逻辑代码中，抛出异常自动实现重试 （默认是无数次重试）
>
> 2. 应该对RabbitMQ重试次数实现限制，比如最多重试5次，每次间隔3s；重试多次还是失败的情况下，存放到死信队列或者存放到数据库表中记录后期人工补偿

#### 二、如何合理选择消息重试

> 1. 消费者获取消息后，调用第三方接口，但是调用第三方接口失败呢？是否需要重试 ？
>
> 2. 消费者获取消息后，代码问题抛出数据异常，是否需要重试？
>
> 总结：如果消费者处理消息时，因为代码原因抛出异常是需要从新发布版本才能解决的，那么就不需要重试，重试也解决不了该问题的。存放到死信队列或者是数据库表记录、后期人工实现补偿。

#### 三、SpringBoot开启重试策略

```yaml
spring:
  rabbitmq:
    ####连接地址
    host: 192.168.75.130
    ####端口号
    port: 5672
    ####账号
    username: admin
    ####密码
    password: admin
    ### 地址
    virtual-host: /wmh
    listener:
      simple:
        retry:
          ####开启消费者（程序出现异常的情况下会）进行重试
          enabled: true
          ####最大重试次数
          max-attempts: 5
          ####重试间隔次数
          initial-interval: 3000
```

##### 1.邮件消费者开启重试策略

```java
System.out.println("邮件消费者消息msg:" + msg);
JSONObject msgJson = JSONObject.parseObject(msg);
String email = msgJson.getString("email");
String emailUrl = "http://127.0.0.1:8081/sendEmail?email=" + email;
JSONObject jsonObject = null;
try {
    jsonObject = HttpClientUtils.httpGet(emailUrl);
} catch (Exception e) {
    String errorMsg = email + ",调用第三方邮件接口失败:" + ",错误原因:" + e.getMessage();
    throw new Exception(errorMsg);
}
System.out.println("邮件消费者调用第三方接口结果:" + jsonObject);
```

##### 2. SpringBoot开启消息确认机制   

消息者开启手动ack

```java
int result = orderMapper.addOrder(orderEntity);
if (result >= 0) {
    // 开启消息确认机制
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
}
```

application.yml中添加：

```yaml
listener:
  simple:
    retry:
      ####开启消费者（程序出现异常的情况下会）进行重试
      enabled: true
      ####最大重试次数
      max-attempts: 5
      ####重试间隔次数
      initial-interval: 3000
  ###开启ack模式
    acknowledge-mode: manual
```

##### 3.rabbitMQ如何解决消息幂等问题 

> 采用消息全局id根据业务来定

生产者：

```java
RequestMapping("/sendOrderMsg")
    public String sendOrderMsg() {
        // 1.生产订单id
        String orderId = System.currentTimeMillis() + "";
        String orderName = "测试订单名字-------------";
        OrderEntity orderEntity = new OrderEntity(orderName, orderId);
        String msg = JSONObject.toJSONString(orderEntity);
        sendMsg(msg, orderId);
        return orderId;
        // 后期客户端主动使用orderId调用服务器接口 查询该订单id是否在数据库中存在数据 消费成功 消费失败
    }

    @Async
    public void sendMsg(String msg, String orderId) {
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msg,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
//                        message.getMessageProperties().setExpiration("10000");
                        message.getMessageProperties().setMessageId(orderId);
                        return message;
                    }
                });
        // 消息投递失败
    }
```

消费者：

```java
String msg = new String(message.getBody());
System.out.println("订单队列获取消息:" + msg);
OrderEntity orderEntity = JSONObject.parseObject(msg, OrderEntity.class);
if (orderEntity == null) {
    return;
}
// messageId根据具体业务来定,如果已经在数据表中插入过数据，则不会插入
String orderId = message.getMessageProperties().getMessageId();
if (StringUtils.isEmpty(orderId)) {
    // 开启消息确认机制
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    return;
}
OrderEntity dbOrderEntity = orderMapper.getOrder(orderId);
if (dbOrderEntity != null) {
    // 说明已经处理过请求
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    return;
}

int result = orderMapper.addOrder(orderEntity);
if (result >= 0) {
    // 开启消息确认机制
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    int i = 1 / 0;
}
```





