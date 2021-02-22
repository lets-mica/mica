# mica-xss 组件

## 说明
- 对表单绑定的字符串类型进行 xss 处理。
- 对 json 字符串数据进行 xss 处理。
- 提供路由和控制器方法级别的放行规则。
- 对表单和 json 字符串 trim 处理。

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-xss</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-xss:${version}")
```

## 配置
| 配置项                         | 默认值 | 说明                                        |
| ------------------------------ | ------ | ----------------------------------------- |
| mica.xss.enabled               | true   | 开启xss                                   |
| mica.xss.trim-text             | true   | 【全局】是否去除文本首尾空格                  |
| mica.xss.mode                  | clear  | 模式：clear 清理（默认），escape 转义        |
| mica.xss.pretty-print          | false  | `clear 专用` prettyPrint，默认关闭： 保留换行  |
| mica.xss.enable-escape         | false  | `clear 专用` 转义，默认关闭                   |
| mica.xss.path-patterns         | `/**`  | 拦截的路由，例如: `/api/order/**`             |
| mica.xss.path-exclude-patterns |        | 放行的路由，默认为空                           |

## 注解
可以使用 `@XssCleanIgnore` 注解对方法和类级别进行忽略。

## 自定义 xss 清理
如果内置的 xss 清理规则不满足需求，可以自己实现 `XssCleaner`，注册成 Spring bean 即可。

### 1. 注册成 Spring bean
```java
@Bean
public XssCleaner xssCleaner() {
    return new MyXssCleaner();
}
```

### 2. MyXssCleaner
```java
public class MyXssCleaner implements XssCleaner {

	@Override
	public String clean(String html) {
		Document.OutputSettings settings = new Document.OutputSettings()
			// 1. 转义用最少的规则，没找到关闭的方法
			.escapeMode(Entities.EscapeMode.xhtml)
			// 2. 保留换行
			.prettyPrint(false);
		// 注意会被转义
		String escapedText = Jsoup.clean(html, "", XssUtil.WHITE_LIST, settings);
		// 3. 反转义
		return Entities.unescape(escapedText);
	}

}
```