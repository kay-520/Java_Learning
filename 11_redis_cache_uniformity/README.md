## Mysql与缓存数据一致性问题

问题；如果数据库数据发生了变化，如何将变化的数据同步给redis？

> 1.直接删除redis缓存，见代码
>
> 2.基于MQ形式实现同步（略）
>
> 3.基于canal订阅binlog二进制文件，通过mq实现异步同步

#### 基于canal订阅binlog同步

原理：

> 1.canal服务器伪装成mysql的从节点，订阅mysql 的binlog二进制文件
>
> 2.当mysql主节点binlog发生改变时，会通知给canal服务器端
>
> 3.canal服务器将改变的数据转换成json数据发送canal客户端
>
> 4.在canal客户端中，将数据异步的写入到redis中

##### 环境搭建

1.配置mysql，开启binlog主从同步

```properties
#1.查询mysql安装目录
select @@datadir;

#2.然后在my.cnf文件添加以下配置，并重启服务器
log-bin=mysql-bin #添加这一行就ok
binlog-format=ROW #选择row模式 
server_id=1

#3.查询是否开启binlog
show variables like 'log_bin';

#4.添加用户并赋予权限（略）
```

2.配置canal服务端（此处为了方便使用docker安装）

```shell
docker pull canal/canal-server:latest
docker run -p 11111:11111 --name canal -id canal/canal-server
docker exec -it canal /bin/bash
# 修改canal.id=2  不能与之前的mysql配置id相同
vi /home/admin/canal-server/conf/canal.properties
#修改mysql主节点地址
vi /home/admin/canal-server/conf/example/instance.properties
```

3.测试代码



