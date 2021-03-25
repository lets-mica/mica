# mica-logging（logback 的日志扩展）

## 规则
1. 默认情况下，打印 `console` 日志、`all.log`、`error.log`。
2. 设置为 `json` 格式化，打印 `console` 日志（非 `json`）、`all.log`（`json` 格式，可用于 `filebeat` 收集）。
3. 开启 `logstash` 只打印 `console` 日志，并将日志输出到 `logstash`。
4. 可配置启动完成关闭 `console` 日志。

## 功能
1. 默认日志配置。
2. 打印 json 日志文件。
3. logstash 日志收集。
4. 启动完成关闭控制台日志。

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
**注意：** 开启 `json` 文件或 `logstash` **必须添加**该依赖！！！

```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>${version}</version>
</dependency>
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

| 配置项                                 | 默认值    | 说明                                                         |
| -------------------------------------- | --------- | ------------------------------------------------------------ |
| mica.logging.console.close-after-start | false     | 是否启动完成后将自动关闭控制台日志，默认**false**。**非开发环境**建议设置为 **true** |
| mica.logging.files.enabled             | true      | 是否开启日志文件 `all.log` 和 `error.log`                    |
| mica.logging.files.use-json-format     | false     | 使用 json 格式化，设置后文件打印 json 日志，可用于 filebeat 收集日志文件 |
| mica.logging.logstash.enabled          | false     | 是否开启 logstash 日志收集，直接收集到 logstash              |
| mica.logging.logstash.host             | localhost | logstash host                                                |
| mica.logging.logstash.port             | 5000      | logstash port                                                |
| mica.logging.logstash.queue-size       | 512       | logstash 队列大小                                            |

## 参考
- [jhipster](https://github.com/jhipster/jhipster)
