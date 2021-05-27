# mica-logging（logback 的日志扩展）

## 规则
1. 默认情况下，打印 `console` 日志、`all.log`、`error.log`。
2. 设置为 `json` 格式化，打印 `console` 日志（非 `json`）、`all.log`（`json` 格式，可用于 `filebeat` 收集）。
3. 开启 `logstash`，打印 `console` 日志，并将日志输出到 `logstash`（建议关闭掉 `file` 输出）。
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

## 配置
| 配置项                                 | 默认值    | 说明                                                         |
| -------------------------------------- | --------- | ------------------------------------------------------------ |
| mica.logging.console.close-after-start | false     | 是否启动完成后将自动关闭控制台日志，默认**false**。**非开发环境**建议设置为 **true** |
| mica.logging.files.enabled             | true      | 是否开启日志文件 `all.log` 和 `error.log`                    |
| mica.logging.files.use-json-format     | false     | 使用 json 格式化（需 logstash-logback-encoder 依赖），设置后文件打印 json 日志，可用于 filebeat 收集日志文件 |

**特别注意：** 需要配置`服务名`和`环境`，例如：

```yaml
spring:
  application:
    name: mica-test
  profiles:
    active: dev
```

## logstash 日志

### 依赖项
- 开启 `json` 文件或 `logstash` **必须添加**该依赖！！！

```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>${version}</version>
</dependency>
```

- `logstash` 默认使用的 `disruptor` 异步，额外还需要添加依赖

```xml
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>${version}</version>
</dependency>
```

### 配置项
| 配置项                                 | 默认值    | 说明                                                         |
| -------------------------------------- | --------- | ------------------------------------------------------------ |
| mica.logging.logstash.enabled          | false     | 是否开启 logstash 日志收集，直接收集到 logstash              |
| mica.logging.logstash.destinations     | localhost:5000 | 目标地址，默认： localhost:5000，示例： host1.domain.com,host2.domain.com:5560 |
| mica.logging.logstash.queue-size       | 512       | logstash 队列大小                                            |

## loki 日志收集

### 依赖项
- java8

```xml
<dependency>
    <groupId>com.github.loki4j</groupId>
    <artifactId>loki-logback-appender-jdk8</artifactId>
    <version>1.2.0</version>
</dependency>
```

- java11

```xml
<dependency>
    <groupId>com.github.loki4j</groupId>
    <artifactId>loki-logback-appender</artifactId>
    <version>1.2.0</version>
</dependency>
```

- 可选依赖 OkHttp

```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>3.14.9</version>
</dependency>
```

- 可选依赖 apache httpclient

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>
```

- 可选依赖 Protobuf (ProtoBuf 日志编码必须)

```xml
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>3.15.1</version>
</dependency>
<dependency>
    <groupId>org.xerial.snappy</groupId>
    <artifactId>snappy-java</artifactId>
    <version>1.1.8.4</version>
</dependency>
```

### 配置项
| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| mica.logging.loki.enabled | false | 是否开启 loki 日志收集 |
| mica.logging.loki.batch-max-bytes | 0 |  |
| mica.logging.loki.batch-max-items | 1000 | 通用配置 |
| mica.logging.loki.batch-timeout-ms | 60000 |  |
| mica.logging.loki.drain-on-stop | true |  |
| mica.logging.loki.encoder | json | 编码方式 Json 或 ProtoBuf |
| mica.logging.loki.format-label-pattern | `appName=${appName},profile=${profile},host=${HOSTNAME},level=%level,traceId=%X{traceId:-NAN},requestId=%X{requestId:-NAN}` | format 配置 |
| mica.logging.loki.format-label-pattern-extend |  | format 配置扩展格式同 format-label-pattern |
| mica.logging.loki.format-label-pair-separator | , |  |
| mica.logging.loki.format-label-key-value-separator | = |  |
| mica.logging.loki.format-label-no-pex | true |  |
| mica.logging.loki.format-message-pattern | l=%level c=%logger{20} t=%thread %msg %ex | 消息体格式 |
| mica.logging.loki.format-sort-by-time | false |  |
| mica.logging.loki.format-static-labels | false |  |
| mica.logging.loki.http-auth-username |  |  |
| mica.logging.loki.http-auth-password |  |  |
| mica.logging.loki.http-auth-tenant-id |  |  |
| mica.logging.loki.http-url |  | http 配置，默认：http://localhost:3100/loki/api/v1/push |
| mica.logging.loki.http-sender |  | http sender，支持 java11、OKHttp、ApacheHttp，默认: 从项目依赖中查找，顺序 java11 -> okHttp -> ApacheHttp |
| mica.logging.loki.http-connection-timeout-ms | 30000 |  |
| mica.logging.loki.http-request-timeout-ms | 5000 |  |
| mica.logging.loki.metrics-enabled | false | 开启 metrics |
| mica.logging.loki.send-queue-max-bytes | 41943040 |  |
| mica.logging.loki.use-direct-buffers | true | 使用堆外内存 |
| mica.logging.loki.verbose | false |  |

## 日志示例
### 文件日志
```shell
2021-03-25 21:03:55.275  INFO 2354 --- [XNIO-1 task-3] n.d.mica.mybatis.logger.SqlLogFilter     :

======= Sql Logger ======================
select id, parent_id, title, name, seq , path, permission, component, icon, is_frame , type, cache, hidden, status, remark , created_by, created_at, updated_by, updated_at from sys_menu where type in (0, 1) and hidden = false and status = 0
======= Sql Execute Time: 3.438ms =======
```

### json 日志
```json
{"loggingLevelRoot":"info","appName":"mica-fast","profile":"dev","level":"INFO","logger_name":"o.s.b.w.e.u.UndertowWebServer","message":"Undertow started on port(s) 8080 (http)","thread_name":"main","@timestamp":"2021-03-25T13:10:34.371Z"}
```

### logstash 日志（stdout）
```json
{
                "port" => 57146,
            "@version" => "1",
             "profile" => "dev",
         "logger_name" => "net.dreamlu.mica.mybatis.logger.SqlLogFilter",
             "appName" => "mica-fast",
         "thread_name" => "XNIO-1 task-3",
               "level" => "INFO",
         "level_value" => 20000,
             "message" => "\n\n======= Sql Logger ======================\nselect id, parent_id, title, name, seq , path, permission, component, icon, is_frame , type, cache, hidden, status, remark , created_by, created_at, updated_by, updated_at from sys_menu where type in (0, 1) and hidden = false and status = 0\n======= Sql Execute Time: 3.438ms =======\n",
    "loggingLevelRoot" => "info",
                "host" => "localhost",
           "requestId" => "d17d635f0a479f01f846199231008ec9",
          "@timestamp" => 2021-03-25T13:03:55.275Z
}
```

## 日志服务（云服务接入）
- 阿里云日志服务（json格式）：https://help.aliyun.com/document_detail/31720.htm
- 腾讯云云日志服务（json格式）：https://cloud.tencent.com/document/product/614/17419

## 链接
- [loki 日志配置详细说明](https://loki4j.github.io/loki-logback-appender/docs/configuration)

## 参考
- [jhipster](https://github.com/jhipster/jhipster)
