## mica（云母）
[![Mica Maven release](https://img.shields.io/nexus/r/https/oss.sonatype.org/net.dreamlu/mica-bom.svg?style=flat-square)](https://mvnrepository.com/artifact/net.dreamlu/mica-bom)
[![Mica maven snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/net.dreamlu/mica-bom.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/net/dreamlu/mica-bom)

`Mica`，Spring Cloud 微服务开发核心包，支持 `web` 和 `webflux`。

`注意`：`snapshots` 版本会及时响应，修复最新的 `bug` 或者必要的需求。

| 依赖         | 版本              |
| ------------ | ----------------- |
| Spring Boot  | 2.1.x             |
| Spring Cloud | Greenwich.RELEASE |

![犬夜叉-云母](docs/img/mica-001.jpeg)

想要了解更多可加入【如梦技术】QQ群：`479710041`。

## 模块划分
### mica-auto
1. 编译期生成 `spring.factories`。
2. 编译期生成 `spring-devtools.properties`。
3. 编译期生成 `FeignClient` 信息到 `spring.factories` 中，供 `mica-pro` 中完成 `Feign` 自动化配置。

`源码地址`：[https://gitee.com/596392912/mica-auto](https://gitee.com/596392912/mica-auto)

### mica-core
- 常用工具包，基于 `Spring-core` 扩展增强，无其他依赖。
- 增强 cglib Bean copy，高性能（接近直接get set），支持链式 bean，支持类型转换 。
- `$` 工具类快捷方法，不用再记忆到底有哪些工具类。
- 统一消息返回体，封装得更加好用。
- Jaskson Read Write HttpMessageConverter，分读写的消息转换器。
- Spring 枚举转换器，规则同 Jackson。

### mica-launcher
- 项目启动器
- 启动信息打印
- 系统环境处理
- spi 扩展

### mica-boot
- 支持 `Spring boot web` 和 `Spring boot webflux`。
- 异步配置。
- 异常处理，未知异常发送 Event 事件，方便监听收集。
- swagger自动化配置，加入jar包即可。
- jackson配置。
- 文件上传配置。
- 文件下载，支持断点续传，浏览器兼容好。
- 请求日志打印，方便开发。
- url 版本号和 header 版本处理。

### mica-boot-test
- 方便 mica-boot 测试，注入 mica-launcher 中注入的参数。

### mica-log4j2
- mica log4j配置。
- 基于 disruptor 异步日志，高性能。
- 非开发环境将 System.out 和 err 写入 log。

### mica-captcha
- 验证码，支持 `webflux` 和 `serlvet`。

### mica-cloud
- Feign 自动降级、header 透传、版本处理，结合 `mica-auto` 自动化配置。
- RestTemplate自动配置，基于okhttp增强，添加请求日志和Header传递。
- hystrix 熔断器增强，支持 header 透传、当前用户获取和透传。
- 本地开发不注册到 Eureka 服务中，避免影响联调环境。
- Apollo Properties 配置刷新。

### mica-plus-error-catch
- 未知异常收集到 spring-cloud-stream 中，方便统一处理。

### mica-plus-redis
- redis cache name # 自动配置超时时间。

### mica-plus-mongo
- mongo 复杂 tree 和 jsonNode 转换处理。

## 已知问题
lombok 生成的 method 问题：https://github.com/rzwitserloot/lombok/issues/1861

对于 xX 类属性名，第一个小写，第二个大写的 bean 属性名，Map -> Bean 或 Bean -> Map 存在问题。

不打算做兼容，待 lombok 新版修复。

## 协议
![LGPL v3](docs/img/lgplv3-147x51.png) 

## 用户权益
允许以引入不改源码的形式免费用于学习、毕设、公司项目、私活等。

特殊情况修改代码，但仍然想闭源需经过作者同意。

参考请注明：参考自 mica：https://github.com/lets-mica/mica

`注意`：若禁止条款被发现有权追讨19999的授权费。

## 授权用户（最佳实践）
* `pigx` 宇宙最强微服务（架构师必备）：https://pig4cloud.com
* `bladex` 完整的线上解决方案（企业生产必备）：https://bladex.vip

## 相关链接
* `示例项目`：[https://github.com/lets-mica/mica-example](https://github.com/lets-mica/mica-example)
* mica 源码 Github：[https://github.com/lets-mica](https://github.com/lets-mica)
* mica 源码 Gitee（码云）：[https://gitee.com/596392912/mica](https://gitee.com/596392912/mica)
* 文档地址（官网）：[https://www.dreamlu.net/docs/](https://www.dreamlu.net/docs/)
* 文档地址（语雀-可关注订阅）：[https://www.yuque.com/dreamlu/mica](https://www.yuque.com/dreamlu/mica)

## 开源推荐
- `Avue` 一款基于vue可配置化的神奇框架：[https://gitee.com/smallweigit/avue](https://gitee.com/smallweigit/avue)
- `pig` 宇宙最强微服务（架构师必备）：[https://gitee.com/log4j/pig](https://gitee.com/log4j/pig)
- `SpringBlade` 完整的线上解决方案（企业开发必备）：[https://gitee.com/smallc/SpringBlade](https://gitee.com/smallc/SpringBlade)
- `IJPay` 支付SDK让支付触手可及：[https://gitee.com/javen205/IJPay](https://gitee.com/javen205/IJPay)

## 鸣谢
感谢 `如梦技术VIP群` 小伙伴们的支持。

## 微信公众号

![如梦技术](docs/img/dreamlu-weixin.jpg)

精彩内容每日推荐！!