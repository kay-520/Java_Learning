maven打包项目到docker之前需要先安装jdk环境，以及registry 
##### 安装registry2
```shell script
docker run -d -p 5000:5000 --restart=always --name registry2 registry:2
## 用vim编辑器修改docker.service文件
vi /usr/lib/systemd/system/docker.service
## 需要修改的部分：
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
## 修改后的部分：
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock
## 重启服务
```
##### 安装jdk环境
下载：jdk-8u231-linux-x64.tar.gz

```properties
## Dockerfile
# 指定基镜像 centos
FROM centos
 
# 复制jdk到指定目录
ADD jdk-12.0.1_linux-x64_bin.tar.gz /usr/local/src/
RUN ln -s /usr/local/src/jdk-12.0.1/ /usr/local/jdk
# 配置jdk环境
ENV JAVA_HOME /usr/local/jdk
ENV JRE_HOME $JAVA_HOME/jre
ENV CLASSPATH $JAVA_HOME/lib/:$JRE_HOME/lib/
ENV PATH $PATH:$JAVA_HOME/bin
 
# 暴露8080端口
EXPOSE 80
# 检测java版本，确认是否安装成功
RUN java -version
```
将jdk压缩包和dockerfile上传到服务器上，执行命令
```shell script
docker build -t wmh/java:1.8 .
```




##### 命令
```shell script
mvn clean package docker:build
```


