### RabitMQ消息中间件

#### 一、介绍

RabbitMQ是实现了高级消息队列协议（AMQP）的开源消息代理软件（亦称面向消息的中间件），RabbitMQ服务器是用Erlang语言编写的。 [RabitMQ官方网站](https://www.rabbitmq.com/)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191204112346312.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

1.点对点模式（简单）

2.工作模式  （能者多劳）

3.发布订阅

4.路由模式

5.topic模式（通配符）

6.RPC模式

7.发布者确定模式



####  二、安装

> 1.下载并安装erlang,下载地址：http://www.erlang.org/download
>
> 2.配置erlang环境变量信息
>
> 新增环境变量ERLANG_HOME=erlang的安装地址
>
> 将%ERLANG_HOME%\bin加入到path中
>
> 3.下载并安装RabbitMQ，下载地址：http://www.rabbitmq.com/download.html
>
> 注意: RabbitMQ 它依赖于Erlang,需要先安装Erlang。
>
> 

#### 三、管理平台

> 管理平台地址 http://127.0.0.1:15672
>
> 默认账号:guest/guest
>
> Virtual Hosts：虚拟消息服务器，exchange、queue、message不能互通。像mysql有数据库的概念并且可以指定用户对库和表等操作的权限。
>
> 默认的端口15672：rabbitmq管理平台端口号
>
> 默认的端口5672： rabbitmq消息中间内部通讯的端口
>
> 默认的端口号25672 rabbitmq集群的端口号



#### 四、快速入门

##### 1.一对一（简单队列）

maven依赖：

```xml
<dependencies>
    <dependency>
        <groupId>com.rabbitmq</groupId>
        <artifactId>amqp-client</artifactId>
        <version>3.6.5</version>
    </dependency>
</dependencies>
```

连接工具类：

```java
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: RabitMQConnection.java
 * @description: 获取连接
 * @date:2019/12/4
 **/
public class RabitMQConnection {

    public static Connection getConnection() throws IOException, TimeoutException {
        //1.创建连接
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置连接地址
        factory.setHost("127.0.0.1");
        //3.设置端口
        factory.setPort(5672);
        //4.设置账号密码
        factory.setUsername("karma");
        factory.setPassword("karma");
        //5.设置VirtualHost
        factory.setVirtualHost("/wmh");
        return factory.newConnection();
    }
}
```

生产者：

```java
package com.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: Producer.java
 * @description: 生产者
 * @date:2019/12/4
 **/
public class Producer {
    private static final String QUEUE_NAME = "karma";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        Channel channel = connection.createChannel();
        //3.设置消息
        String msg = "RabbitMq 简单队列测试消息！！！";
        System.out.println("msg:" + msg);
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        channel.close();
        connection.close();
    }
}
```

消费者：

```java
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: Consumer.java
 * @description: 消费者
 * @date:2019/12/4
 **/
public class Consumer {
    private static final String QUEUE_NAME = "karma";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        Channel channel = connection.createChannel();
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("msg:" + msg);
            }
        };
        //3.监听队列 true为自动获取
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
```

***RabbitMQ如何保证消息不丢失***

> 1.使用消息确定机制+持久化机制
>
> a.消息确定收到消息机制
>
> ```java
> channel.basicConsume(QUEUE_NAME, false, defaultConsumer);
> ```
>
> 注：第二个参数值为false代表关闭RabbitMQ的自动应答机制，改为手动应答。
>
> 在处理完消息时，返回应答状态，true表示为自动应答模式。
>
> ```shell
> channel.basicAck(envelope.getDeliveryTag(), false);
> ```
>
> b.生产者确认投递消息成功 使用Confirm机制
>
> ```java
> //3.开启消息确定机制
> channel.confirmSelect();
> //4.设置消息
> String msg = "RabbitMq 简单队列测试消息！！！";
> System.out.println("msg:" + msg);
> channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
> if (channel.waitForConfirms()){
> 	System.out.println("消息发送成功！");
> }else{
> 	System.out.println("消息发送失败！");
> }
> ```
>
> 2.RabbitMQ默认创建是持久化的
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191206141638965.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)
>
> Durability是否持久化：durable为持久化、 Transient 不持久化
>
> autoDelete 是否自动删除：当最后一个消费者断开连接之后队列是否自动被删除，可以通过RabbitMQ Management，查看某个队列的消费者数量，当consumers = 0时队列就会自动删除

###### 1.1 生成者确认消息投递到MQ

```java
public class Producer {
    private static final String QUEUE_NAME = "karma";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        Channel channel = connection.createChannel();
        //3.开启消息确定机制
        channel.confirmSelect();
        //4.设置消息
        String msg = "RabbitMq 简单队列测试消息！！！";
        System.out.println("msg:" + msg);
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        if (channel.waitForConfirms()){
            System.out.println("消息发送成功！");
        }else{
            System.out.println("消息发送失败！");
        }
        channel.close();
        connection.close();
    }
}
```

###### 1.2 消费者确定消息消费成功

```java
public class Consumer {
    private static final String QUEUE_NAME = "karma";
    private static int serviceTimeOut = 1000;

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        final Channel channel = connection.createChannel();
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("消费消息msg:" + msg);
                //手动ack应答模式
                channel.basicAck(envelope.getDeliveryTag(), false);
                try {
                    Thread.sleep(serviceTimeOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //3.监听队列 true为自动获取 f
        channel.basicConsume(QUEUE_NAME, false, defaultConsumer);
    }
}
```

##### 2.工作队列

> 默认的传统队列是为均摊消费的，存在不公平性；如果每个消费者消费的速度不一样的话，均摊消费是不公平的，应该为能者多劳。
>
> 只需在通道中设置basicQos为1即可，表示MQ服务器每次会给消费者推送一条消息，必须手动ack确定之后才回继续发送。
>
> channel.basicQos(1);

##### 3.发布订阅

> 生产者发送一条消息，经过交换机转发到多个不同的队列，多个不同的队列就多个不同的消费者。
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191206145207609.jpg)

生产者：

```java
public class ProducerFanout {
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        //通道关联交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
        String msg = "发布订阅测试消息！！！！！！！";
        channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
        channel.close();
        connection.close();
    }
}
```

短信消费者：

```java
public class SmsConsumer {
    /**
     * 定义短信队列
     */
    private static final String QUEUE_NAME = "consumerFanout_sms";
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("短信消费者...");
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        final Channel channel = connection.createChannel();
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("短信消费消息msg:" + msg);
            }
        };
        //3.监听队列 true为自动获取 false手动应答
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
```

邮件消费者：

```java
public class MailConsumer {
    /**
     * 定义短信队列
     */
    private static final String QUEUE_NAME = "consumerFanout_email";
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("邮件消费者...");
        //1.创建连接
        Connection connection = RabitMQConnection.getConnection();
        //2.设置通道
        final Channel channel = connection.createChannel();
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("邮件消费消息msg:" + msg);
            }
        };
        //3.监听队列 true为自动获取 false手动应答
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
```

***RabbitMQ交换机类型***

Direct exchange（直连交换机）

Fanout exchange（扇型交换机）

Topic exchange（主题交换机）

Headers exchange（头交换机）

##### 4.Direct路由模式

> 当交换机类型为Direct类型时，根据队列绑定的路由键将消息转发到具体的队列。
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191215162258810.png)

生产者：

```java
public class ProducerDirect {
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("生产者启动成功..");
        Connection connection = RabitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        //通道关联交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        String msg = "生产者发送消息内容" + System.currentTimeMillis();
        channel.basicPublish(EXCHANGE_NAME, "sms", null, msg.getBytes());
        channel.close();
        connection.close();
    }

}
```

邮件消费者：

```java
public class EmailConsumer {
    /***
     * 定义邮件队列
     */
    private static final String QUEUE_NAME = "direct_email";
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("邮件消费者...");
        // 创建我们的连接
        Connection connection = RabitMQConnection.getConnection();
        // 创建我们通道
        final Channel channel = connection.createChannel();
        // 关联队列消费者关联队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "email"); //参数3为队列所绑定的路由键
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("邮件消费者获取消息:" + msg);
            }
        };
        // 开始监听消息 自动签收
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }
}
```

短信消费者：

```java
public class SmsConsumer {
    /**
     * 定义短信队列
     */
    private static final String QUEUE_NAME = "direct_sms";
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("短信消费者...");
        // 创建我们的连接
        Connection connection = RabitMQConnection.getConnection();
        // 创建我们通道
        final Channel channel = connection.createChannel();
        // 关联队列消费者关联队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "sms");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("短信消费者获取消息:" + msg);
            }
        };
        // 开始监听消息 自动签收
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }
}
```

##### 5.Topic主题模式

> 当交换机类型为topic类型时，根据队列绑定的路由键模糊转发到具体的队列中存放
>
> #表示支持匹配多个词
>
> *表示只能匹配一个词
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191215165731280.png)

生产者：

```java
public class ProducerTopic {
    private static final String EXCHANGE_NAME = "topic_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("生产者启动成功..");
        Connection connection = RabitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        //通道关联交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        String msg = "生产者发送消息内容" + System.currentTimeMillis();
        channel.basicPublish(EXCHANGE_NAME, "wmh.email", null, msg.getBytes());//参数2为路由键
        channel.close();
        connection.close();
    }
}
```

消费者：

```java
public class EmailConsumer {
    private static final String QUEUE_NAME = "topic_email";
    private static final String EXCHANGE_NAME = "topic_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("邮件消费者...");
        Connection connection = RabitMQConnection.getConnection();
        final Channel channel = connection.createChannel();
        // 关联队列消费者关联队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "wmh.*");//参数3为队列所绑定的路由键
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("邮件消费者获取消息:" + msg);
            }
        };
        // 开始监听消息 自动签收
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
```

#### 五、SpringBoot整合

Maven依赖

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
```

配置类：

```java
public class RabbitMQConfig {
    /**
     * 定义交换机
     */
    private String EXCHANGE_SPRINGBOOT_NAME = "springboot_exchange";


    /**
     * 短信队列
     */
    private String FANOUT_SMS_QUEUE = "fanout_sms_queue";

    /**
     * 邮件队列
     */
    private String FANOUT_EMAIL_QUEUE = "fanout_email_queue";

    /***
     * 创建短信队列
     * @return
     */
    @Bean
    public Queue smsQueue() {
        return new Queue(FANOUT_SMS_QUEUE);
    }

    /***
     * 创建邮件队列
     * @return
     */
    @Bean
    public Queue emailQueue(){
        return new Queue(FANOUT_EMAIL_QUEUE);
    }


    /***
     * 创建交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE_SPRINGBOOT_NAME);
    }

    /***
     * 定义短信队列绑定交换机
     * @param smsQueue
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding smsBindingExchange(Queue smsQueue,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(smsQueue).to(fanoutExchange);
    }

    /***
     * 定义邮件队列绑定交换机
     * @param emailQueue
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding emailBindingExchange(Queue emailQueue,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(emailQueue).to(fanoutExchange);
    }
}
```

Controller层：（消费生产）

```java
@RestController
public class FanoutProducerController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /***
     * 向rabbitmq中发送消息（发布订阅模式）
     * @param msg
     * @return
     */
    @RequestMapping("/sendMsg")
    public String sendMsg(String msg) {
        //参数1 交换机名称 、参数2路由key  参数3 消息
        amqpTemplate.convertAndSend("springboot_exchange", "", msg);
        return "success";
    }

    @RequestMapping("/sendMsg2")
    public String sendMsg(Integer msg) {
        amqpTemplate.convertAndSend("springboot_exchange", "", msg);
        return "success";
    }


    /***
     * 手动消费短信消息
     * @return
     */
    @RequestMapping("/receiveMsg2")
    public String receiveMsg() {
        String fanout_sms_queue = amqpTemplate.receiveAndConvert("fanout_sms_queue").toString();
        return "接收到消息："+fanout_sms_queue;
    }
}
```

消息监听，有消息时则会进行接收并处理

```java
@Component
@RabbitListener(queues = {"fanout_email_queue"})//队列中有消息时则会进行接收并处理
public class FanoutEmailConsumer {

    //@RabbitListener 标注在类上面表示当有收到消息的时候，
    // 就交给 @RabbitHandler 的方法处理，具体使用哪个方法处理，
    // 根据 MessageConverter 转换后的参数类型
    @RabbitHandler
    public void process(String msg) {
        System.out.println("邮件消费者消息String类型msg:" + msg);
    }

    @RabbitHandler
    public void process2(Integer msg) {
        System.out.println("邮件消费者消息Integer类型msg:" + msg);
    }
}
```

