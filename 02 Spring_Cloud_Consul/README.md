consul_config----->consul配置中心
consul_consumer------>consul消费者 （Fegin客户端调用、RestTemplate、断路器Fallback）
consul_provider------->consul生产者
D:\Project\Study\Java_Learning\02 Spring_Cloud_Consul\consul_config\遇到的一些坑.md
```shell script
## consul启动命令
consul agent -dev -ui -node=consul-dev -client=192.168.75.128
```
