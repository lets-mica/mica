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

jsoup 可选依赖，用来将 html 转换成 java Bean。
```xml
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>${jsoup.version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-http:${version}")
```

jsoup 可选依赖，用来将 html 转换成 java Bean。
```groovy
compile("org.jsoup:jsoup:${jsoupVersion}")
```


### 使用文档
```java
// 设定全局日志级别 NONE，BASIC，HEADERS，BODY， 默认：NONE
HttpRequest.setGlobalLog(LogLevel.BODY);

// 同步请求 url，方法支持 get、post、patch、put、delete
HttpRequest.get("https://www.baidu.com")
    .log(LogLevel.BASIC)             //设定本次的日志级别，优先于全局
    .addHeader("x-account-id", "mica001") // 添加 header
    .addCookie(new Cookie.Builder()  // 添加 cookie
        .name("sid")
        .value("mica_user_001")
        .build()
    )
    .query("q", "mica") //设置 url 参数，默认进行 url encode
    .queryEncoded("name", "encodedValue")
    .formBuilder()    // 表单构造器，同类 multipartFormBuilder 文件上传表单
    .add("id", 123123) // 表单参数
    .execute()                      // 发起请求
    .asJsonNode();                  // 结果集转换，注：如果网络异常等会直接抛出异常。
// 同类的方法有 asString、asBytes
// json 类响应：asJsonNode、asValue、asList、asMap，采用 jackson 处理
// xml、html响应：asDomValue、asDomList 采用的 jsoup 处理，需要添加 Jsoup 依赖。
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
```

### DomMapper 工具

`DomMapper` 工具采用 `cglib` 动态代理和 `Jsoup` html 解析，不到 `200` 行代码实现了 `html` 转 `java Bean` 工具，爬虫必备。  

主要方法有：
- DomMapper.asDocument
- DomMapper.readDocument
- DomMapper.readValue
- DomMapper.readList

### CssQuery 注解说明
```java
public @interface CssQuery {

	/**
	 * CssQuery
	 *
	 * @return CssQuery
	 */
	String value();

	/**
	 * 读取的 dom attr
	 *
	 * <p>
	 * attr：元素对于的 attr 的值
	 * html：整个元素的html
	 * text：元素内文本
	 * allText：多个元素的文本值
	 * </p>
	 *
	 * @return attr
	 */
	String attr() default "";

	/**
	 * 正则，用于对 attr value 处理
	 *
	 * @return regex
	 */
	String regex() default "";

	/**
	 * 默认的正则 group
	 */
	int DEFAULT_REGEX_GROUP = 0;

	/**
	 * 正则 group，默认为 0
	 *
	 * @return regexGroup
	 */
	int regexGroup() default DEFAULT_REGEX_GROUP;

	/**
	 * 嵌套的内部模型：默认 false
	 *
	 * @return 是否为内部模型
	 */
	boolean inner() default false;
}
```

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

### 示例代码3
#### 爬取开源中国首页
```java
// 同步，异常返回 null
Oschina oschina = HttpRequest.get("https://www.oschina.net")
    .execute()
    .onSuccess(responseSpec -> responseSpec.asDomValue(Oschina.class));
if (oschina == null) {
    return;
}
System.out.println(oschina.getTitle());

System.out.println("热门新闻");

List<VNews> vNews = oschina.getVNews();
for (VNews vNew : vNews) {
    System.out.println("title:\t" + vNew.getTitle());
    System.out.println("href:\t" + vNew.getHref());
    System.out.println("时间:\t" + vNew.getDate());
}

System.out.println("热门博客");
List<VBlog> vBlogList = oschina.getVBlogList();
for (VBlog vBlog : vBlogList) {
    System.out.println("title:\t" + vBlog.getTitle());
    System.out.println("href:\t" + vBlog.getHref());
    System.out.println("阅读数:\t" + vBlog.getRead());
    System.out.println("评价数:\t" + vBlog.getPing());
    System.out.println("点赞数:\t" + vBlog.getZhan());
}
```

#### 模型1
```java
@Getter
@Setter
public class Oschina {

	@CssQuery(value = "head > title", attr = "text")
	private String title;

	@CssQuery(value = "#v_news .page .news", inner = true) // 标记为嵌套模型
	private List<VNews> vNews;

	@CssQuery(value = ".blog-container .blog-list div", inner = true) // 标记为嵌套模型
	private List<VBlog> vBlogList;

}
```

#### 模型2
```java
@Setter
@Getter
public class VNews {

	@CssQuery(value = "a", attr = "title")
	private String title;

	@CssQuery(value = "a", attr = "href")
	private String href;

	@CssQuery(value = ".news-date", attr = "text")
	@DateTimeFormat(pattern = "MM/dd")
	private Date date;

}
```

#### 模型3
```java
@Getter
@Setter
public class VBlog {

	@CssQuery(value = "a", attr = "title")
	private String title;

	@CssQuery(value = "a", attr = "href")
	private String href;

	//1341阅/9评/4赞
	@CssQuery(value = "span", attr = "text", regex = "^\\d+")
	private Integer read;

	@CssQuery(value = "span", attr = "text", regex = "(\\d*).*/(\\d*).*/(\\d*).*", regexGroup = 2)
	private Integer ping;

	@CssQuery(value = "span", attr = "text", regex = "(\\d*).*/(\\d*).*/(\\d*).*", regexGroup = 3)
	private Integer zhan;

}
```