# mica-http
`mica-http` 是 `okhttp` 的封装，Fluent 语法的 http 工具包，语法参考 HttpClient Fluent API。

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-http</artifactId>
  <version>${version}</version>
</dependency>
```

spring-retry 为可选依赖，用来对 http 结果断言重试。
```xml
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
    <version>${spring-retry.version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-http:${version}")
```

spring-retry 为可选依赖，用来对 http 结果断言重试。
```groovy
compile("org.springframework.retry:spring-retry:${springRetryVersion}")
```
