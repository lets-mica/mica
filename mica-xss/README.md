# mica-xss 组件

## 说明
- 对表单绑定的字符串类型进行 xss 处理。
- 对 json 字符串数据进行 xss 处理。
- 提供路由和控制器方法级别的放行规则。

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
| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| mica.xss.enabled | true | 开启xss |
| mica.xss.path-patterns |  | 拦截的路由，必须配置，例如: /api/order/** |
| mica.xss.exclude-patterns |  | 放行的规则，默认为空 |

## 注解
可以使用 `@XssCleanIgnore` 注解对方法和类级别进行忽略。