# mica-okhttp
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
private String getUserEmail(String accessToken) {
    return HttpRequest.get("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))")
            .addHeader("Host", "api.linkedin.com")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Authorization", "Bearer " + accessToken)
            .log()
            .execute()
            .asJsonNode()
            .at("/elements/0/handle~0/emailAddress")
            .asText();
}

public static void test() {
	HttpRequest.post("https://www.baidu.com/do-stuff")
		.log(HttpLoggingInterceptor.Level.BASIC)
		.formBuilder()
		.add("a", "b")
		.execute()
		.asBytes();
}
```
