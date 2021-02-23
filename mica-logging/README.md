# mica-logging（logback 的日志扩展）

## 功能
1. 默认日志配置。
2. logstash 日志收集。
3. 启动完成关闭控制台日志。

## 依赖引用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-logging</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-logging:${version}")
```

## 可选依赖
### maven
```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.logstash.logback:logstash-logback-encoder:${version}")
```

## 配置
**特别注意：** 需要配置`服务名`和`环境`，例如：

```yaml
spring:
  application:
    name: mica-test
  profiles:
    active: dev
```

| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| mica.logging.console.enabled | true | 是否开启控制台日志，默认**开启**。设置为`关闭`时，启动完成后将自动关闭控制台日志 |
| mica.logging.logstash.enabled | false | 是否开启 logstash 日志收集 |
| mica.logging.logstash.host | localhost | logstash host |
| mica.logging.logstash.port | 5000 | logstash port |
| mica.logging.logstash.queue-size | 512 | logstash 队列大小 |
| mica.logging.use-json-format | false | 使用 json 格式化 |

## 参考
- [jhipster](https://github.com/jhipster/jhipster)