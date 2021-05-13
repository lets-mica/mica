# mica-jetcache

## 功能扩展
- jackson KeyConvertorParser、encoder、decoder
- 添加 spring-configuration-metadata.json
- 扩展 metrics 打通 micrometer

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-jetcache</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-jetcache:${version}")
```

### 可选依赖 spring data redis
```xml
<dependency>
    <groupId>com.alicp.jetcache</groupId>
    <artifactId>jetcache-redis-springdata</artifactId>
    <version>2.6.0</version>
</dependency>
```

### 可选依赖 jedis
```xml
<dependency>
    <groupId>com.alicp.jetcache</groupId>
    <artifactId>jetcache-redis</artifactId>
    <version>2.6.0</version>
</dependency>
```

**注意:** 可选依赖，必选其一。

## 配置项
| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| jetcache.metrics.enabled | true | 开启 jetcache metrics，默认：true |
| jetcache.metrics.enabled-stat-info-logger | false | 开启 StatInfoLogger |
| jetcache.metrics.verbose-log | false | StatInfoLogger 打印明细，默认：false |

## 配置示例
```yaml
jetcache:
  statIntervalMinutes: 1
  areaInCacheName: false
  local:
    default:
      type: caffeine # 还支持：linkedhashmap
      keyConvertor: bean:jacksonKeyConvertor
  remote:
    default:
      type: redis.springdata
      keyConvertor: bean:jacksonKeyConvertor
      valueEncoder: bean:jacksonValueEncoder # 支持：kryo、java 
      valueDecoder: bean:jacksonValueDecoder # 支持：kryo、java
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: 127.0.0.1
      port: 6379
```

## jetcache 官方文档
更多详细使用文档请见： https://github.com/alibaba/jetcache