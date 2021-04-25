# mica-jetcache

## 功能扩展
- 扩展 metrics 打通 micrometer
- jackson KeyConvertorParser
- 添加 spring-configuration-metadata.json

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
      type: caffeine
      keyConvertor: bean:jacksonKeyConvertor
  remote:
    default:
      type: redis.springdata
      keyConvertor: bean:jacksonKeyConvertor
      valueEncoder: bean:jacksonValueEncoder
      valueDecoder: bean:jacksonValueDecoder
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: 127.0.0.1
      port: 6379
```
