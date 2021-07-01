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

## 基础用法
```java
// 设定全局日志级别 NONE，BASIC，HEADERS，BODY， 默认：NONE 和 self4
HttpRequest.setGlobalLog(LogLevel.BODY);
// 设置控制台日志，用于没有日志依赖的 sdk 开发时使用
HttpRequest.setGlobalLog(HttpLogger.Console, LogLevel.BODY);
```

```java
// 同步请求 url，方法支持 get、post、patch、put、delete
HttpRequest.get("https://www.baidu.com")
    .useSlf4jLog() // 使用 Slf4j 日志，同类的有 .useConsoleLog(),日志级别为 BODY
    .addHeader("x-account-id", "mica001") // 添加 header
    .addCookie(builder -> builder.domain("www.baidu.com").name("name").value("value"))  // 添加 cookie
    .query("q", "mica") // 设置 url 参数，默认进行 url encode
    .queryEncoded("name", "encodedValue")
    .formBuilder()    // 表单构造器，同类 multipartFormBuilder 文件上传表单
    .add("id", 123123) // 表单参数
    .retryOn(responseSpec -> !responseSpec.isOk()) // 结合 spring retry 进行结果集断言
    .proxy(InetSocketAddress.createUnresolved("127.0.0.1", 8080)) // 设置代理
    .execute()                      // 发起请求
    .asJsonNode();                  // 结果集转换，注：如果网络异常等会直接抛出异常。
// 同类的方法有 asString、asBytes
// json 类响应：asJsonNode、asValue、asList、asMap、atJsonPath、，采用 jackson 处理
// file 文件：toFile

// 同步
String html = HttpRequest.post("https://www.baidu.com")
    .execute()
    .onFailed((request, e) -> {// 网络等异常情况的消费处理，可无
        e.printStackTrace();
    })
    .onResponse(ResponseSpec::asString);// 处理响应，有网络异常等直接返回 null

// 同步
Document document = HttpRequest.patch("https://www.baidu.com")
    .execute()
    .onSuccess(DomMapper::asDocument);
// onSuccess http code in [200..300) 处理响应，有网络异常等直接返回 null

// 发送异步请求
HttpRequest.delete("https://www.baidu.com")
    .async() // 开启异步
    .onFailed((request, e) -> {    // 异常时的处理
        e.printStackTrace();
    })
    .onResponse(responseSpec -> {  // 消费响应， 注意：响应的流只能读一次
        int httpCode = responseSpec.code();

    })
    .onSuccessful(responseSpec -> { // 消费响应成功 http code in [200..300)
        // 注意：响应结果流只能读一次
        JsonNode jsonNode = responseSpec.asJsonNode();
    })
    .execute(); // 异步最后发起请求

// cookie 管理，另外可以自定义实现 okhttp 的 CookieJar
InMemoryCookieManager cookieManager = new InMemoryCookieManager();
HttpRequest.get("https://demo.dreamlu.net/captcha.jpg")
    .cookieManager(cookieManager)
    .execute()
    .asString();
```

## 示例
### 示例代码1
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
        .useConsoleLog(LogLevel.HEADERS)    // 日志级别
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

### 示例证书配置
```java
InputStream isTrustCa = HttpRequestTest.class.getResourceAsStream("/cert/ca.jks");
InputStream isSelfCert = HttpRequestTest.class.getResourceAsStream("/cert/outgoing.CertwithKey.pkcs12");

KeyStore selfCert = KeyStore.getInstance("pkcs12");
selfCert.load(isSelfCert, "password".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
kmf.init(selfCert, "password".toCharArray());
KeyStore caCert = KeyStore.getInstance("jks");
caCert.load(isTrustCa, "caPassword".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
tmf.init(caCert);
SSLContext sc = SSLContext.getInstance("TLS");

TrustManager[] trustManagers = tmf.getTrustManagers();
X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
sc.init(kmf.getKeyManagers(), trustManagers, (SecureRandom) null);

// 1. 全局配置证书
OkHttpClient.Builder builder = new OkHttpClient.Builder()
	.sslSocketFactory(sc.getSocketFactory(), trustManager)
	.hostnameVerifier(TrustAllHostNames.INSTANCE);
HttpRequest.setHttpClient(builder.build());

// 2. 单次请求配置证书
HttpRequest.get("https://123.xxx")
	.useConsoleLog(LogLevel.BODY)
	.sslSocketFactory(sc.getSocketFactory(), trustManager)
	.disableSslValidation()
	.execute()
	.asString();
```

### 示例上传流
```java
public static void main(String[] args) {
    // 设置全局日志级别
    HttpRequest.setGlobalLog(HttpLogger.Console, LogLevel.HEADERS);
    // 1. 下载文件流，注意： mica-http CompletableFuture 异步不会自动关流，其他都会自动关闭
    InputStream inputStream = HttpRequest.get("http://www.baidu.com/img/PCdong_eab05f3d3a8e54ca5a0817f09b39d463.gif")
        .executeAsyncAndJoin()
        .asStream();
    // 2. 上传文件流
    String html = HttpRequest.post("http://1.w2wz.com/upload.php")
        .multipartFormBuilder()
        // 上传流，上传完毕后会自动关闭流
        .add("uploadimg", "test.gif", inputStream)
        .execute()
        .asString();
    System.out.println(html);
}
```