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

### gradle
```groovy
compile("net.dreamlu:mica-http:${version}")
```

### 示例代码
```java
// 设置全局日志级别
HttpRequest.setGlobalLog(LogLevel.BODY);

// 直接用 jackson json path 语法
private String getUserEmail(String accessToken) {
    return HttpRequest.get("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))")
            .addHeader("Host", "api.linkedin.com")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Authorization", "Bearer " + accessToken)
            .execute()
            .asJsonNode()
            .at("/elements/0/handle~0/emailAddress")
            .asText();
}

// 异步
public static void test() {
    HttpRequest.post("https://www.baidu.com/do-stuff")
        .log(LogLevel.BASIC)                // 日志级别
        .formBuilder()                      // 表单构造器
        .add("a", "b")
        .async()                            // 使用异步
        .onSuccessful(System.out::println)  // 异步成功时的函数
        .onFailed((request, e) -> {         // 异步失败，可无
            e.printStackTrace();
        });
}
```
