# laytpl 模板（将其引入到 java 中）

具体可见 [《震惊，java8 Nashorn和laytpl居然能擦出这样火花！》](https://my.oschina.net/qq596392912/blog/872813)

## 注意
jmh 实测性能不是很出色，约为 `Thymeleaf` 的 `1/2` 适合用于对性能不是特别高的场景。例如：代码生成等。

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-laytpl</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-laytpl:${version}")
```

## 使用
```java
@Autowired
private MicaTemplate micaTemplate;
```

```java
Map<String, Object> data = new HashMap<>();
data.put("title", "mica");

String html = micaTemplate.render("<h3>{{ d.title }}</h3>", data);
```

```java
Map<String, Object> data = new HashMap<>();
data.put("title", "mica");

// renderTpl 将渲染 classpath:templates/tpl/ 下的模板文件
String html = micaTemplate.renderTpl("test.html", data);
```

## 模版语法

| 语法              | 说明                                                         | 
| ----------------- | ------------------------------------------------------------ |
| {{ d.field }}     | 输出一个普通字段，不转义html                                 |
| {{= d.field }}    | 输出一个普通字段，并转义html                                 |
| {{# JS表达式 }}    | JS 语句。一般用于逻辑处理。用分隔符加 # 号开头。注意：如果你是想输出一个函数，正确的写法是：{{ fn() }}，而不是：{{# fn() }} |
| {{! template !}}  | 对一段指定的模板区域进行过滤，即不解析该区域的模板。 |

## 文档

### 内置对象
| 对象     | 说明    |
| ------- | ------- |
| console | 同 js console 可使用 console.log 打印日志，采用 Slf4j 做的嫁接 |
| fmt     | 格式化时间或者数字  fmt.format( d.date ))，或者 fmt.format( d.date, "yyyy-MM-dd" )) 自定义格式。|
| mica    | 使用 mica.use("sec") 在模板中使用 spring bean |

### 日志和格式
```html
{{#
console.log();

console.log("im {}", "L.cm");

console.error("hi im {}", "L.cm");

console.log("laytpl version:{}", laytpl.v);

console.log(fmt.format( d.date ));
}}
```

### 使用 Spring bean
```html
测试tpl中使用spring bean:

直接使用 mica.use("sec"); 方法传参beanName，即可。

示例：
<br>
{{# mica.use("sec").hasPermission('admin:add') }}
<br>
```
