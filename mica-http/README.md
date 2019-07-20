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
        })
        .execute();
}
```

### 示例代码2
```java
HttpRequest.setGlobalLog(LogLevel.BODY);

// 同步，异常时 返回 null
String html = HttpRequest.get("www.baidu.com")
    .connectTimeout(Duration.ofSeconds(1000))
    .query("test", "a")
    .query("name", "張三")
    .query("x", 1)
    .query("abd", Base64Util.encode("123&$#%"))
    .queryEncoded("abc", Base64Util.encode("123&$#%"))
    .execute()
    .onFailed(((request, e) -> {
        e.printStackTrace();
    }))
    .onSuccess(ResponseSpec::asString);
System.out.println(html);

// 同步调用，返回 Optional，异常时返回 Optional.empty()
Optional<String> opt = HttpRequest.post(URI.create("https://www.baidu.com"))
    .bodyString("Important stuff")
    .formBuilder()
    .add("a", "b")
    .execute()
    .onSuccessOpt(ResponseSpec::asString);

// 同步，成功时消费（处理） response
HttpRequest.post("https://www.baidu.com/some-form")
    .addHeader("X-Custom-header", "stuff")
    .execute()
    .onSuccessful(responseSpec -> {
        String text = responseSpec.asString();
        System.out.println(text);
    });

// 同步，异常时直接抛出
HttpRequest.get("https://www.baidu.com/some-form")
    .execute()
    .asString();

// async，异步执行结果，失败时打印堆栈
HttpRequest.get("https://www.baidu.com/some-form")
    .async()
    .onSuccessful(System.out::println)
    .onFailed((request, e) -> {
        e.printStackTrace();
    })
    .execute();
```