# mica-redis
- redis cache 增强，支持 # 号分隔 cachename 和 超时，支持 ms（毫秒），s（秒默认），m（分），h（小时），d（天）等单位。
- 基于 redis 的分布式限流组件。
- redis key 失效事件自动配置。

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

## 配置

| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| mica.redis.key-expired-event.enable | false | 是否启用redis key 失效事件，默认：关闭 |
| mica.redis.rate-limiter.enable | false | 是否开启 redis 分布式限流，默认：关闭 |
| mica.redis.serializer-type | JSON | 序列化方式 JSON、JDK，需要自定义实现 `RedisSerializer` 即可，默认：JSON |
| mica.redis.stream.enable | false | 是否开启 stream |
| mica.redis.stream.consumer-group |  | consumer group，默认：服务名 + 环境 |
| mica.redis.stream.consumer-name |  | 消费者名称，默认：ip + 端口 |
| mica.redis.stream.poll-batch-size |  | poll 批量大小 |
| mica.redis.stream.poll-timeout |  | poll 超时时间 |

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

### 3. 示例 redis key 超时事件（监听）

```java
@Async
@EventListener(RedisKeyExpiredEvent.class)
public void redisKeyExpiredEvent(RedisKeyExpiredEvent<Object> event) {
    String redisKey = new String(event.getId());
    System.out.println(redisKey);
}
```

### 4. 示例 redis stream 使用

#### 4.1 发送
```java
@Autowired
RStreamTemplate streamTemplate
```

方法：

```java

/**
 * 发布消息
 *
 * @param name  队列名
 * @param value 消息
 * @return 消息id
 */
RecordId send(String name, Object value);

/**
 * 发布消息
 *
 * @param name  队列名
 * @param key   消息key
 * @param value 消息
 * @return 消息id
 */
RecordId send(String name, String key, Object value);

/**
 * 批量发布
 *
 * @param name     队列名
 * @param messages 消息
 * @return 消息id
 */
RecordId send(String name, Map<String, Object> messages);

/**
 * 发送消息
 *
 * @param record Record
 * @return 消息id
 */
RecordId send(Record<String, ?> record);

/**
 * 删除消息
 *
 * @param name      stream name
 * @param recordIds recordIds
 * @return Long
 */
@Nullable
Long delete(String name, String... recordIds);

/**
 * 删除消息
 *
 * @param name      stream name
 * @param recordIds recordIds
 * @return Long
 */
@Nullable
Long delete(String name, RecordId... recordIds);

/**
 * 删除消息
 *
 * @param record Record
 * @return Long
 */
@Nullable
Long delete(Record<String, ?> record);

/**
 * 对流进行修剪，限制长度
 *
 * @param name  name
 * @param count count
 * @return Long
 */
@Nullable
Long trim(String name, long count);

/**
 * 对流进行修剪，限制长度
 *
 * @param name                name
 * @param count               count
 * @param approximateTrimming
 * @return Long
 */
@Nullable
Long trim(String name, long count, boolean approximateTrimming);
```

#### 4.2 监听

##### 4.2.1 注解方式

```java
@RStreamListener(name = "order")
public void order(Record<String, OrderDto> record) {
    // 业务逻辑
}
```

`@RStreamListener` 注解属性：

```java
public @interface RStreamListener {

	/**
	 * Queue name
	 *
	 * @return String
	 */
	String name();

	/**
	 * consumer group，默认为服务名 + 环境
	 *
	 * @return String
	 */
	String group() default "";

	/**
	 * 消息方式，集群模式和广播模式，如果想让所有订阅者收到所有消息，广播是一个不错的选择。
	 *
	 * @return MessageModel
	 */
	MessageModel messageModel() default MessageModel.CLUSTERING;

	/**
	 * offsetModel，默认：LAST_CONSUMED
	 *
	 * <p>
	 * 0-0 : 从开始的地方读。
	 * $ ：表示从尾部开始消费，只接受新消息，当前 Stream 消息会全部忽略。
	 * > : 读取所有新到达的元素，这些元素的id大于消费组使用的最后一个元素。
	 * </p>
	 *
	 * @return ReadOffsetModel
	 */
	ReadOffsetModel offsetModel() default ReadOffsetModel.LAST_CONSUMED;

	/**
	 * 自动 ack
	 *
	 * @return boolean
	 */
	boolean autoAcknowledge() default false;

}
```

##### 4.2.2 Bean方式

```java

import org.springframework.stereotype.Component;

@Component
public class OrderListener implements RPubSubListenerCustomizer {
    @Override
    public List<Topic> getTopics() {
        return List.of(ChannelTopic.of("order_1"), ChannelTopic.of("order_2"));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if ("order_1".equals(new String(message.getChannel()))) {
            // 业务逻辑
        }
        if ("order_2".equals(new String(message.getChannel()))) {
            // 业务逻辑
        }
    }
}
```

### 5. 示例 redis pubsub 使用

#### 5.1 发布
```java
@Autowired
RPubSubPublisher pubSubPublisher;

/**
 * 测试发送
 */
public void test(OrderDto message) {
    pubSubPublisher.publish("test", message);
}
```

#### 5.2 监听

#### 5.2.1 注解方式
```java
@RPubSubListener(name = "test")
public void test(RPubSubEvent<OrderDto> event) {
// 业务逻辑
}
```

#### 5.2.2 Bean方式
```java
public class TestListener implements RPubSubListenerCustomizer {
    @Override
    public List<Topic> getTopics() {
        return List.of(ChannelTopic.of("test"));
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        if ("test".equals(new String(message.getChannel()))) {
            // 业务逻辑
        }
    }
}
```

## 拓展链接
- Redis windows 服务端：https://github.com/tporadowski/redis/releases
- Redis windows 客户端管理工具：https://github.com/lework/RedisDesktopManager-Windows/releases
