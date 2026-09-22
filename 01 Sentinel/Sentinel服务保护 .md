### 一、Sentinel 介绍

随着微服务的流行，服务和服务之间的稳定性变得越来越重要。 https://github.com/alibaba/Sentinel[Sentinel] 以流量为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。

https://github.com/alibaba/Sentinel[Sentinel] 具有以下特征:

* *丰富的应用场景*： Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、实时熔断下游不可用应用等。
* *完备的实时监控*： Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。
* *广泛的开源生态*： Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、Dubbo、gRPC 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。
* *完善的 SPI 扩展点*： Sentinel 提供简单易用、完善的 SPI 扩展点。您可以通过实现扩展点，快速的定制逻辑。例如定制规则管理、适配数据源等。

#### 服务保护的基本概念

服务限流/熔断

> 在高并发情况下，客户端请求达到了一定的极限，也就是我们所设定的阈值，服务就会自动开启自我保护机制，直接走我们的服务降级fallback方法，给客户端一个友好提示。

服务降级

> 在高并发情况下，为了防止用户一直等待，给用户一个友好提示。

服务的雪崩效应

> 默认情况下，tomcat/jetty服务器只有一个线程池去处理用户请求。在高并发情况下，如果客户端的所有请求都堆积在同一个接口上，线程池中的所有线程都用来处理这些请求，就会导致其他接口无法访问。

服务隔离机制

> 服务隔离机制分为两种：信号量隔离和线程池隔离
>
> 信号量隔离：最多只能有一定的阈值的线程数来处理我们的请求，超过阈值就会拒绝请求
>
> 线程池隔离：每个服务接口都有独立的线程池来处理请求，接口之间互不影响，缺点：占用CPU资源较大

Sentinel和Hytrix区别

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200206160035900.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

### 二、Sentinel 环境快速搭建

下载对应Sentinel-Dashboard

https://github.com/alibaba/Sentinel/releases/tag/1.7.1 运行即可。

默认账号密码：sentinel/sentinel

运行执行命令

```shell
java -Dserver.port=8718 -Dcsp.sentinel.dashboard.server=localhost:8718 -Dproject.name=sentinel-dashboard -Dcsp.sentinel.api.port=8719 -jar sentinel-dashboard-1.7.1.jar
```

### 三、Sentinel实现API动态限流

maven依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-alibaba-sentinel</artifactId>
    <version>0.2.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### 1.手动配置管理API限流接口

```java
private static final String GETORDER_KEY = "getOrder";

@RequestMapping("/getOrder")
public String getOrders() {
    Entry entry = null;
    try {
        entry = SphU.entry(GETORDER_KEY);
        // 执行我们服务需要保护的业务逻辑
        return "getOrder接口";
    } catch (Exception e) {
        e.printStackTrace();
        return "该服务接口已经达到上线!";
    } finally {
        // SphU.entry(xxx) 需要与 entry.exit() 成对出现,否则会导致调用链记录异常
        if (entry != null) {
            entry.exit();
        }
    }
}
```

限流配置放到项目自动加载

```java
@Component
@Slf4j
public class SentinelApplicationRunner implements ApplicationRunner {
    private static final String GETORDER_KEY = "getOrder";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(GETORDER_KEY);
        // QPS控制在2以内
        rule1.setCount(1);
        // QPS限流
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
        log.info(">>>限流服务接口配置加载成功>>>");
    }
}
```

#### 2.注解形式配置管理Api限流

##### 2.1 基于QPS限流

```java
private static final String GETORDER_KEY = "getOrder";

    /***
     * @SentinelResource 流量规则资源名
     * - blockHandler 限流/熔断 出现异常 执行的方法
     * - fallback 服务降级执行的方法
     * -QPS=并发数/平均响应时间
     * @return
     */
    @SentinelResource(value = GETORDER_KEY,blockHandler = "getOrderQpsException")
    @RequestMapping("/getOrder")
    public String getOrders() {
        return "getOrder接口";
    }

    /**
     * 被限流后返回的提示
     *
     * @param e
     * @return
     */
    public String getOrderQpsException(BlockException e) {
        e.printStackTrace();
        return "该接口已经被限流啦!";
    }
```

application.yml

```yaml
server:
  port: 8080
spring:
  cloud:
    sentinel:
      transport:
        port: 8719
        dashboard: 192.168.75.137:8718
      eager: true
  application:
    name: Sentinel-02
```

Sentinel控制台中添加限流规则

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200206160057275.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)



##### 2.2 基于并发数限流

```java
    /***
     * 基于并发数量处理限流
     * 并发数 = QPS*平均响应时间
     * 每次最多只会有一个线程处理该业务逻辑，超出该阈值的情况下，直接拒绝访问。
     * @return
     */
    @SentinelResource(value = "getOrderThrad", blockHandler = "getOrderQpsException")
    @RequestMapping("/getOrderThrad")
    public String getgetOrderThread(){
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "getOrderThrad";
    }
```

Sentinel控制台中添加限流规则

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200206160105139.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

如果要在您的项目中引入 Sentinel，使用 group ID 为 `com.alibaba.cloud` 和 artifact ID 为 `spring-cloud-starter-alibaba-sentinel` 的 starter。

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

下面这个例子就是一个最简单的使用 Sentinel 的例子:

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}

@RestController
public class TestController {

    @GetMapping(value = "/hello")
    @SentinelResource("hello")
    public String hello() {
        return "Hello Sentinel";
    }

}

```

@SentinelResource 注解用来标识资源是否被限流、降级。上述例子上该注解的属性 'hello' 表示资源名。

@SentinelResource 还提供了其它额外的属性如 `blockHandler`，`blockHandlerClass`，`fallback` 用于表示限流或降级的操作，更多内容可以参考 https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81[Sentinel注解支持]。

以上例子都是在 WebServlet 环境下使用的，Sentinel 目前已经支持 WebFlux，需要配合 `spring-boot-starter-webflux` 依赖触发 sentinel-starter 中 WebFlux 相关的自动化配置。

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

}

@RestController
public class TestController {

    @GetMapping("/mono")
    @SentinelResource("hello")
    public Mono<String> mono() {
	return Mono.just("simple string")
			.transform(new SentinelReactorTransformer<>("otherResourceName"));
    }

}

```

#### 3. 基于热点参数手动实现限流

添加maven依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-parameter-flow-control</artifactId>
    <version>1.6.3</version>
    <scope>compile</scope>
</dependency>

```

将限流规则，手动放入到项目启动自动加载:

```java
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

```

controller层：

```java
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
```

#### 4. 基于热点参数控制台实现限流

java代码：

```java
@RestController
public class IndexController {
    private static final String GETORDER_KEY = "getOrder";

    @GetMapping("getOrder")
    @SentinelResource(GETORDER_KEY)
    public String getOrder(int id) {
        return GETORDER_KEY + ".id=" + id;
    }
}
```

application.yml

```yaml
server:
  port: 8080
spring:
  cloud:
    sentinel:
      transport:
        port: 8719
        dashboard: 192.168.75.137:8718
      eager: true
```

Sentinel控制台中添加热点参数限流规则：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200212213200263.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

### 四、Sentinel实现API熔断降级

#### 4.1 基于平均响应时间

java代码：

```java
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
     * 基于平均响应时间 的降级方法
     * @return
     */
    public String getException(){
        return "服务降级啦，当前服务器请求次数过多，请稍后重试!";
    }
```

Sentinel控制台：

每秒平均响应时间超过阈值200ms，走服务降级，并且5秒内无法访问服务，继续走服务降级方法。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209154703230.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

#### 4.2 基于异常比例

java代码：

```java
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
     * 参数必须与被降级的方法中的参数一致，才能实现降级
     * 异常比例和异常次数 的降级方法
     * @param i
     * @return
     */
    public String getException(int i){
        return "服务降级啦，请稍后重试!";
    }
```

Sentinel控制台：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209154647543.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

#### 4.3 基于异常次数 

java代码：

```java
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
        return "服务降级啦，请稍后重试!";
    }
```

Sentinel控制台：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209154629685.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

### 五、RestTemplate 支持

Spring Cloud Alibaba Sentinel 支持对 `RestTemplate` 的服务调用使用 Sentinel 进行保护，在构造 `RestTemplate` bean的时候需要加上 `@SentinelRestTemplate` 注解。

```java
@Bean
@SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class)
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

`@SentinelRestTemplate` 注解的属性支持限流(`blockHandler`, `blockHandlerClass`)和降级(`fallback`, `fallbackClass`)的处理。

其中 `blockHandler` 或 `fallback` 属性对应的方法必须是对应 `blockHandlerClass` 或 `fallbackClass` 属性中的静态方法。

该方法的参数跟返回值跟 `org.springframework.http.client.ClientHttpRequestInterceptor#interceptor` 方法一致，其中参数多出了一个 `BlockException` 参数用于获取 Sentinel 捕获的异常。

比如上述 `@SentinelRestTemplate` 注解中 `ExceptionUtil` 的 `handleException` 属性对应的方法声明如下：

```java
public class ExceptionUtil {
    public static ClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException exception) {
        ...
    }
}
```

NOTE: 应用启动的时候会检查 `@SentinelRestTemplate` 注解对应的限流或降级方法是否存在，如不存在会抛出异常

`@SentinelRestTemplate` 注解的限流(`blockHandler`, `blockHandlerClass`)和降级(`fallback`, `fallbackClass`)属性不强制填写。

当使用 `RestTemplate` 调用被 Sentinel 熔断后，会返回 `RestTemplate request block by sentinel` 信息，或者也可以编写对应的方法自行处理返回信息。这里提供了 `SentinelClientHttpResponse` 用于构造返回信息。

Sentinel RestTemplate 限流的资源规则提供两种粒度：

* `httpmethod:schema://host:port/path`：协议、主机、端口和路径

* `httpmethod:schema://host:port`：协议、主机和端口

NOTE: 以 `https://www.taobao.com/test` 这个 url 并使用 GET 方法为例。对应的资源名有两种粒度，分别是 `GET:https://www.taobao.com` 以及 `GET:https://www.taobao.com/test`

### 六、动态数据源支持（数据持久化）

Sentinel中提供四种数据源实现数据持久化：File、Nacos、Zookeeper、Apolle

配置案例：

```
spring.cloud.sentinel.datasource.ds1.file.file=classpath: degraderule.json
spring.cloud.sentinel.datasource.ds1.file.rule-type=flow

#spring.cloud.sentinel.datasource.ds1.file.file=classpath: flowrule.json
#spring.cloud.sentinel.datasource.ds1.file.data-type=custom
#spring.cloud.sentinel.datasource.ds1.file.converter-class=org.springframework.cloud.alibaba.cloud.examples.JsonFlowRuleListConverter
#spring.cloud.sentinel.datasource.ds1.file.rule-type=flow

spring.cloud.sentinel.datasource.ds2.nacos.server-addr=localhost:8848
spring.cloud.sentinel.datasource.ds2.nacos.data-id=sentinel
spring.cloud.sentinel.datasource.ds2.nacos.group-id=DEFAULT_GROUP
spring.cloud.sentinel.datasource.ds2.nacos.data-type=json
spring.cloud.sentinel.datasource.ds2.nacos.rule-type=degrade

spring.cloud.sentinel.datasource.ds3.zk.path = /Sentinel-Demo/SYSTEM-CODE-DEMO-FLOW
spring.cloud.sentinel.datasource.ds3.zk.server-addr = localhost:2181
spring.cloud.sentinel.datasource.ds3.zk.rule-type=authority

spring.cloud.sentinel.datasource.ds4.apollo.namespace-name = application
spring.cloud.sentinel.datasource.ds4.apollo.flow-rules-key = sentinel
spring.cloud.sentinel.datasource.ds4.apollo.default-flow-rule-value = test
spring.cloud.sentinel.datasource.ds4.apollo.rule-type=param-flow

```

每种数据源都有两个共同的配置项： `data-type`、 `converter-class` 以及 `rule-type`。

`data-type` ：表示数据类型，Sentinel 默认提供两种内置的值，分别是 `json` 和 `xml` (默认是json)。 若不想使用这两种，可以添加 `custom` 自定义数据类型，再配置 `converter-class` 配置项，该配置项需要写类的全路径名(比如 `spring.cloud.sentinel.datasource.ds1.file.converter-class=org.springframework.cloud.alibaba.cloud.examples.JsonFlowRuleListConverter`)。

`rule-type` ：表示数据规则，(`flow`，`degrade`，`authority`，`system`, `param-flow`, `gw-flow`, `gw-api-group`)。

#### 6.1 基于Nacos实现数据持久化

添加maven依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
    <version>1.5.2</version>
</dependency>
```

application.yml

```yaml
server:
  port: 8080
spring:
  cloud:
    sentinel:
      transport:
        port: 8719
        dashboard: 192.168.75.137:8718
      eager: true
      datasource:
        ds:
          nacos:
            ### nacos连接地址
            server-addr: 192.168.75.137:8848
            ## nacos连接的分组
            group-id: DEFAULT_GROUP
            ###路由存储规则
            rule-type: flow
            ### 读取配置文件的 data-id
            data-id: Sentinel-03
            ###  读取培训文件类型为json
            data-type: json
      log:
        dir: D:\Code\study\Java_Learning\01 Sentinel\03 基于Nacos实现数据持久化\logs\
  application:
    name: Sentinel-03
```

java代码：

```java
    @SentinelResource(value = "getNacosInfo",blockHandler = "getQpsException")
    @GetMapping("/getNacosInfo")
    public String getNacosInfo(){
        return "getNacosInfo";
    }

    public String getQpsException(){
        return "该接口已被限流，请稍后再试";
    }
```

Nacos控制台：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209154810111.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MDgyNjM0OQ==,size_16,color_FFFFFF,t_70)

> resource：资源名，即限流规则的作用对象
>
> limitApp：流控针对的调用来源，若为 default 则不区分调用来源
>
> grade：限流阈值类型（QPS 或并发线程数）；0代表根据并发数量来限流，1代表根据QPS来进行流量控制
>
> count：限流阈值
>
> strategy：调用关系限流策略

### 七、网关限流

 [https://github.com/alibaba/Sentinel/wiki/%E7%BD%91%E5%85%B3%E9%99%90%E6%B5%81](https://github.com/alibaba/Sentinel/wiki/网关限流) [参考 Sentinel 网关限流]

注：[github源码](https://github.com/karma521/Java_Learning/tree/SpringCloudAlibaba/01%20Sentinel )