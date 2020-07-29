# mica-redis
- redis cache 增强，支持 # 号分隔 cachename 和 超时，支持 ms（毫秒），s（秒默认），m（分），h（小时），d（天）等单位。
- 基于 redis 的分布式限流组件。

## 依赖引用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-redis</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-redis:${version}")
```

## 使用文档

#### 1. redis cache 增强
1. 支持 # 号分隔 cachename 和 超时，支持 ms（毫秒），s（秒默认），m（分），h（小时），d（天）等单位。

示例：
```java
@Cacheable(value = "user#5m", key = "#id")
public String selectById(Serializable id) {
    log.info("selectById");
    return "selectById:" + id;
}
```

### MicaRedisCache
MicaRedisCache 为简化 redis 使用的 bean。
```java
@Autowired
private MicaRedisCache redisCache;

@Override
public String findById(Serializable id) {
    return redisCache.get("user:" + id, () -> userMapper.selectById(id));
}
```

### 2. 分布式限流
#### 2.1 开启限流组件
```yaml
mica:
  redis:
    rate-limiter:
      enable: true
```

### 2.2 使用注解
```java
@RateLimiter
```

注解变量：
```java
/**
 * 限流的 key 支持，必须：请保持唯一性
 *
 * @return key
 */
String value();

/**
 * 限流的参数，可选，支持 spring el # 读取方法参数和 @ 读取 spring bean
 *
 * @return param
 */
String param() default "";

/**
 * 支持的最大请求，默认: 2500
 *
 * @return 请求数
 */
long max() default 2500L;

/**
 * 持续时间，默认: 3600
 *
 * @return 持续时间
 */
long ttl() default 3600L;

/**
 * 时间单位，默认为秒
 *
 * @return TimeUnit
 */
TimeUnit timeUnit() default TimeUnit.SECONDS;
```

#### 2.3 使用 Client
```java
@Autowired
private RateLimiterClient rateLimiterClient;
```

方法：

```java
/**
 * 服务是否被限流
 *
 * @param key 自定义的key，请保证唯一
 * @param max 支持的最大请求
 * @param ttl 时间,单位默认为秒（seconds）
 * @return 是否允许
 */
boolean isAllowed(String key, long max, long ttl);

/**
 * 服务是否被限流
 *
 * @param key      自定义的key，请保证唯一
 * @param max      支持的最大请求
 * @param ttl      时间
 * @param timeUnit 时间单位
 * @return 是否允许
 */
boolean isAllowed(String key, long max, long ttl, TimeUnit timeUnit);

/**
 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
 *
 * @param key      自定义的key，请保证唯一
 * @param max      支持的最大请求
 * @param ttl      时间
 * @param supplier Supplier 函数式
 * @return 函数执行结果
 */
<T> T allow(String key, long max, long ttl, CheckedSupplier<T> supplier);

/**
 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
 *
 * @param key      自定义的key，请保证唯一
 * @param max      支持的最大请求
 * @param ttl      时间
 * @param supplier Supplier 函数式
 * @return 函数执行结果
 */
<T> T allow(String key, long max, long ttl, TimeUnit timeUnit, CheckedSupplier<T> supplier);
```

## 链接
- Redis windows 服务端：https://github.com/tporadowski/redis/releases
- Redis windows 客户端管理工具：https://github.com/lework/RedisDesktopManager-Windows/releases
