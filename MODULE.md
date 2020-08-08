# 模块说明
## mica-auto
1. 编译期生成 `spring.factories`。
2. 编译期生成 `spring-devtools.properties`。
3. 编译期生成 `FeignClient` 信息到 `spring.factories` 中，供 `mica-cloud` 中完成 `Feign` 自动化配置。

`文档地址`：[https://gitee.com/596392912/mica-auto](https://gitee.com/596392912/mica-auto)

## mica-weixin
1. 公众号消息处理、api接口。
2. 小程序消息处理。

`文档地址`：[https://gitee.com/596392912/mica-weixin](https://gitee.com/596392912/mica-weixin)

## mica-core
- 常用工具包，基于 `Spring-core` 扩展增强，无其他依赖。
- 增强 cglib Bean copy，高性能（接近直接 get set），支持链式 bean，支持自动类型转换 。
- `$` 工具类快捷方法，不用再记忆到底有哪些工具类。
- 统一消息返回体，封装得更加好用。
- Spring 枚举转换器，规则同 Jackson。

`文档地址`：[mica-core](https://www.dreamlu.net/docs/utils-common.html)

## mica-http
- `mica-http` 是 `okhttp` 的封装，Fluent 语法的 http 工具包，语法参考 HttpClient Fluent API。

`文档地址`：[mica-http](mica-http/README.md)

## mica-spider
- `mica-spider` 是基于 `mica-http` 的爬虫工具。

`文档地址`：[mica-spider](mica-spider/README.md)

## mica-laytpl 
- laytpl 模板（将其引入到 java 中）

`文档地址`：[mica-laytpl](mica-laytpl/README.md)

## mica-captcha
- 验证码，支持 `webflux` 和 `serlvet`。

`文档地址`：[mica-captcha](mica-captcha/README.md)

## mica-redis
- spring-data-redis 的扩展
- spring cache 支持 # 分割超时时间
- 分布式限流组件

`文档地址`：[mica-redis](mica-redis/README.md)

## mica-mongo
- mongo 复杂 tree 和 jsonNode 转换处理。

`文档地址`：[mica-mongo](mica-mongo/README.md)

## mica-swagger
- `swagger` 和 `knife4j` 依赖，自动化配置。

`文档地址`：[mica-swagger](mica-swagger/README.md)

## mica-ip2region
- ip 获取地址位置扩展

`文档地址`：[mica-ip2region](mica-ip2region/README.md)

## mica-jobs
- xxl job stater，方便使用

`文档地址`：[mica-jobs](mica-jobs/README.md)

## mica-xss
- 对表单绑定的字符串类型进行 xss 处理。
- 对 json 字符串数据进行 xss 处理。
- 提供路由和控制器方法级别的放行规则。

`文档地址`：[mica-xss](mica-xss/README.md)

## mica-metrics
- sentinel 指标收集。
- undertow 指标收集。

`文档地址`：[mica-metrics](mica-metrics/README.md)
