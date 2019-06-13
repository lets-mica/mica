# mica-okhttp
`mica-okhttp` 是基于 `okhttp` 的简单 http 工具包，语法参考 HttpClient Fluent API（fluent-hc）。

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
    return XRequest.get("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))")
            .addHeader("Host", "api.linkedin.com")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Authorization", "Bearer " + accessToken)
            .log()
            .execute()
            .asJsonNode()
            .at("/elements/0/handle~/emailAddress")
            .asText();
}

public static void test() {
	XRequest.post("https://www.baidu.com/do-stuff")
		.log(HttpLoggingInterceptor.Level.BASIC)
		.formBuilder()
		.add("a", "b")
		.execute()
		.asBytes();
}
```
