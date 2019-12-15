### RabbitMQ实战解决方案

#### 一、RabbitMQ死信队列 

> RabbitMQ死信队列俗称，备胎队列；消息中间件因为某种原因拒收该消息后，可以转移到死信队列中存放，死信队列也可以有交换机和路由key等。

产生的原因：

> 1. 消息投递到MQ中存放 消息已经过期
>
> 2. 队列达到最大的长度 （队列容器已经满了）MQ拒绝接受消息
>
> 3. 消费者消费多次消息失败，就会转移存放到死信队列中

#####          1.SpringBoot整合死信队列  

mave依赖：

```xml
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
    </parent>
    <dependencies>

        <!-- springboot-web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 添加springboot对amqp的支持 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>
        <!--fastjson -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.49</version>
        </dependency>
    </dependencies>
```

application.yml

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
server:
  port: 8080

###模拟演示死信队列
#死信队列
dlx:
  exchange: dlx_exchange
  queue: order_dlx_queue
  routingKey: dlx
#订单队列
order:
  exchange: order_exchange
  queue: order_queue
  routingKey: order
```

正常队列及死信队列配置：

```java
@Component
public class DeadLetterMQConfig {
    /**
     * 订单交换机
     */
    @Value("${order.exchange}")
    private String orderExchange;

    /**
     * 订单队列
     */
    @Value("${order.queue}")
    private String orderQueue;

    /**
     * 订单路由key
     */
    @Value("${order.routingKey}")
    private String orderRoutingKey;
    /**
     * 死信交换机
     */
    @Value("${dlx.exchange}")
    private String dlxExchange;

    /**
     * 死信队列
     */
    @Value("${dlx.queue}")
    private String dlxQueue;
    /**
     * 死信路由
     */
    @Value("${dlx.routingKey}")
    private String dlxRoutingKey;

    /**
     * 声明死信交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    /**
     * 声明死信队列
     *
     * @return Queue
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueue);
    }

    /**
     * 声明订单业务交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(orderExchange);
    }

    /**
     * 绑定死信队列到死信交换机
     *
     * @return Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }

    /**
     * 声明订单队列
     *
     * @return Queue
     */
    @Bean
    public Queue orderQueue() {
        // 订单队列绑定我们的死信交换机
        Map<String, Object> arguments = new HashMap<>(2);

        arguments.put("x-dead-letter-exchange", dlxExchange);
        arguments.put("x-dead-letter-routing-key", dlxRoutingKey);
        return new Queue(orderQueue, true, false, false, arguments);
    }

    /**
     * 绑定订单队列到订单交换机
     *
     * @return Binding
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(orderRoutingKey);
    }
}
```

订单消费者：

```java
@Component
public class OrderConsumer {

    /**
     * 监听队列回调的方法
     *
     * @param msg
     */
    @RabbitListener(queues = "order_queue")
    public void orderConsumer(String msg) {
        System.out.println("正常订单消费者消息msg:" + msg);
    }
}
```

死信消费者：

```java
@Component
public class OrderDlxConsumer {

    /**
     * 死信队列监听队列回调的方法
     *
     * @param msg
     */
    @RabbitListener(queues = "order_dlx_queue")
    public void orderConsumer(String msg) {
        System.out.println("死信队列消费订单消息" + msg);
    }
}
```

生产者：

```java
@RestController
public class DeadLetterProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 订单交换机
     */
    @Value("${order.exchange}")
    private String orderExchange;
    /**
     * 订单路由key
     */
    @Value("${order.routingKey}")
    private String orderRoutingKey;

    @RequestMapping("/sendOrder")
    public String sendOrder() {
        String msg = "发送订单测试消息";
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        return "succcess";
    }
}
```

***如若测试死信队列，可将正常订单消费者代码注释。***

#### 二、消息中间件如何获取消费结果

> 根据业务id主动查询

maven依赖：

```xml
<!--fastjson -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.49</version>
</dependency>

<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.1.1</version>
</dependency>
<!-- mysql 依赖 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

核心代码：

```java
@RestController
public class DeadLetterProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 订单交换机
     */
    @Value("${order.exchange}")
    private String orderExchange;
    /**
     * 订单路由key
     */
    @Value("${order.routingKey}")
    private String orderRoutingKey;
    @Autowired
    private OrderMapper orderMapper;

    @RequestMapping("/sendOrder")
    public String sendOrder() {
        String orderId = System.currentTimeMillis() + "";
        String orderName = "测试订单名字----------";
        sendMsg(orderName, orderId);
        return orderId;
    }

    @Async
    public void sendMsg(String orderName, String orderId) {
        OrderEntity orderEntity = new OrderEntity(orderName, orderId);
        String msg = JSONObject.toJSONString(orderEntity);
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
    }

    @RequestMapping("/getOrder")
    public Object getOrder(String orderId) {
        OrderEntity order = orderMapper.getOrder(orderId);
        if (order == null) {
            return "该订单没有被消费或者订单号错误!";
        }
        return order;
    }
}
```

数据访问层：

```java
public interface OrderMapper {
    @Insert("insert order_info values (null,#{orderName},#{orderId})")
    int addOrder(OrderEntity orderEntity);

    @Select("SELECT * from order_info where orderId=#{orderId} ")
    OrderEntity getOrder(String orderId);
}
```

```java
@Data
public class OrderEntity {
    private int id;
    private String orderName;
    private String orderId;

    public OrderEntity(String orderName, String orderId) {
        this.orderName = orderName;
        this.orderId = orderId;
    }

    public OrderEntity() {

    }
}
```

application.yml

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
  datasource:
    url: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
server:
  port: 8080

###模拟演示死信队列
mayikt:
  dlx:
    exchange: dlx_exchange
    queue: order_dlx_queue
    routingKey: dlx
  ###备胎交换机
  order:
    exchange: order_exchange
    queue: order_queue
    routingKey: order
```







