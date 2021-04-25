# mica-jetcache

## 功能扩展
- jackson KeyConvertorParser、encoder、decoder
- 添加 spring-configuration-metadata.json
- 扩展 metrics 打通 micrometer（暂时未实现）

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
