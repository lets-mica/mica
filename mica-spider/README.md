# mica-spider 爬虫组件
`mica-spider` 是基于 `mica-http` 的爬虫工具。

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-spider</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-spider:${version}")
```

## 使用文档

注意：请先阅读 `mica-http` 部分文档。

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

## 示例代码
### 爬取开源中国首页
```java
// 同步，异常返回 null
Oschina oschina = HttpRequest.get("https://www.oschina.net")
    .execute()
	.onSuccess(responseSpec -> DomMapper.readValue(responseSpec.asStream(), Oschina.class));
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

### 模型1
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

### 模型2
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

### 模型3
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