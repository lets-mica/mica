# mica-caffeine
- caffeine cache 增强，支持 # 号分隔 cachename 和 超时，支持 ms（毫秒），s（秒默认），m（分），h（小时），d（天）等单位。

## 依赖引用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-caffeine</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-caffeine:${version}")
```

## 使用文档

#### 1. caffeine cache 增强
1. 支持 # 号分隔 cachename 和 超时，支持 ms（毫秒），s（秒默认），m（分），h（小时），d（天）等单位。

示例：
```java
@Cacheable(value = "user#5m", key = "#id")
public String selectById(Serializable id) {
    log.info("selectById");
    return "selectById:" + id;
}
```
