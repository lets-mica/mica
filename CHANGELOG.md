# 变更记录

## 发行版本
### v1.2.1 - 2019-11-30
- :sparkles: 增强 ThreadLocal 工具类。
- :sparkles: 增强 redis cache 使用。
- :sparkles: 添加 jackson 的类型转换方法。
- :bug: Fixing github #10 mica-http bodyJson 添加 Content-Type.
- :heavy_minus_sign: 移除 YmlPropertyLoaderFactory，隐藏风险，mica 2.0 已研发新的 @MicaPropertySource。
- :heavy_minus_sign: 移除 springfox-swagger-ui 依赖。

### v1.2.0 - 2019-10-13
- :zap: lettuce linux use epoll
- :sparkles: mica-http close ResponseBody。
- :sparkles: mica-actuator Feign client 信息端点。
- :zap: swagger 配置全部移到 swagger 扩展中。
- :zap: 优化 feign auto fallback 集合类型反馈空集合。
- :tada: 添加 mica-actuator 模块，为 mica-admin 做铺垫。

### v1.1.9 - 2019-09-22
- :zap: 添加 mybatis plus 的扩展 mica-plus-mybatis 组件。
- :zap: mica-plus-mybatis 扩展 insert ignore 和 replace 添加老版本的日志记录扩展.
- :zap: 添加分布式锁组件 mica-plus-lock。
- :zap: mica-cloud  ReactiveRequestContextHolder 改为缓存 ServerWebExchange.
- :zap: mica-cloud fallback 优化对空集合的支持.
- :zap: mica-plus-ribbon @世言 同学 pr 的 ribbon 路由 fallback。添加扩展开关，默认关闭.
- :zap: mica-plus-redis protostuff 调成可选依赖.
- :zap: mica-core 添加 Pair.
- :zap: mica-core 添加 MicaExpressionEvaluator.
- :zap: mica-core 添加 YmlPropertyLoaderFactory。
- :zap: mica-core 优化 FileUtil Nio 文件读取.
- :zap: mica-core 提升 FastStringWriter 一倍的性能.
- :zap: mica-http retry 添加对结果集校验.
- :zap: 删除第三方登录推荐直接使用 JustAuth.
- :zap: 减少 codacy 代码检测问题。
- :zap: 优化部分 阿里巴巴 P3C 问题.
- :bug: 修复 mica-core decodeHex java 11 的兼容。
- :bug: mica-http 将 asStream 改成 onStream 避免流未关闭.
- :arrow_up: gradle 升级到 5.6.2。
- :arrow_up: 依赖升级 Spring boot 2.1.8.RELEASE。
- :arrow_up: Spring cloud Greenwich SR3。
- :arrow_up: mybatis-plus 升级到 3.2.0。
- :arrow_up: 依赖升级 swagger-bootstrap-ui 到 1.9.6。

### v1.1.8 - 2019-08-12
- :zap: mica-plus-redis 添加 redis 限流组件.
- :zap: mica-http Response asDocument 方法迁移到 DomMapper，不强制依赖 jsoup.
- :zap: mica-http CssQuery 添加正则取值处理.
- :zap: mica-http 优化 DomMapper 添加更多方法.
- :zap: mica-http proxy 改用 MethodInterceptor.
- :bug: mica-cloud Fixing Feign feignContract mvcConversionService.
- :zap: mica-core 优化 Exceptions 工具，添加 NIO 文件读取.
- :zap: mica-core 拆分 lambda Try 为 Unchecked.
- :bug: 优化 gradle 配置，自动发布 snapshots 版本.
- :building_construction: 迁移 spring-cloud-alibaba 依赖到新版。
- :arrow_up: Spring boot 升级到 2.1.7.RELEASE.

### v1.1.7 - 2019-08-04
- :zap: 优化 readme 添加文档地址.
- :zap: 添加 .codacy.yml 配置.
- :zap: 添加模块 mica-laytpl 模块.
- :zap: mica-core 增强 XmlHelper 支持安全和非安全模式.
- :zap: mica-http html、xml 转 Bean，并支持类型转换.
- :zap: mica-http 添加重试机制.
- :zap: mica-http add EventListener。
- :zap: mica-http 优化拦截器，支持多个，将日志拦截器放到最后。
- :zap: gradle use platform replace enforcedPlatform。
- :zap: mica-cloud 优化 RestTemplate Bean 添加配置开关，默认不开启.
- :arrow_up: 依赖升级 swagger-bootstrap-ui 到 `1.9.5`.

### v1.1.6 - 2019-07-21
- :zap: `mica http` 调整包 由 `net.dreamlu.http` -> `net.dreamlu.mica.http`。
- :zap: `mica http` 去除对 spring 的依赖，使用 jsoup 解析 html 或 xml.
- :zap: `mica-http` 添加异步支持和添加更多使用方法.
- :zap: 完善 `mica-core` CharPool.
- :zap: 简化 `mica-plus-social` 第三方登录使用.
- :zap: 更新 `mica-social` google 授权登录升级接口.
- :sparkles: 更新 `mica-social` 同步 [JustAuth](https://gitee.com/yadong.zhang/JustAuth) 1.9，添加 Teambition、人人、Pinterest、Stack Overflow登录。

### v1.1.5 - 2019-07-14
注意：1.1.3 和 1.1.4 第三方登录，钉钉都有签名问题，请使用最新的 `1.1.5`.
- :zap: 关闭 nacos 默认的 log 配置.
- :sparkles: mica-http 添加 `queryEncoded` 方法.
- :sparkles: mica-http 添加 `HttpRequest.setGlobalLog` 方法，并优化日志级别枚举.
- :bug: mica-bom 中添加 okhttp 版本.
- :bug: 钉钉登录.

### v1.1.3 - 2019-06-16
- :sparkles: 完善 `mica-plus-redis` 添加 `MicaRedisCache` Bean，使用同 `redis` 命令.
- :sparkles: `redis` 序列化方式可配置化.
- :sparkles: 提高 webflux 的日志和 `ReactiveRequestContextFilter` 排序，方便在 spring cloud gateway 中使用.
- :sparkles: 调整验证码 base64，加上 base64 图片前缀.
- :sparkles: `DigestUtil` 对 `Md5`、`Sha`、`Hmac` 等系列签名算法添加非 hex 方法.
- :bug: bean copy Convert 注解支持问题.
- :heavy_plus_sign: 添加新组件 `mica-http`.
- :heavy_plus_sign: 添加新组件 `mica-social`.
- :heavy_plus_sign: 添加新组件 `mica-plus-social`.
- :heavy_plus_sign: 添加新组件 `mica-plus-ribbon`.
- :arrow_up: 升级 `Spring boot` 版本到 `2.1.6.RELEASE`.
- :arrow_up: 升级 `Spring cloud` 版本到 `Greenwich.SR1`.
- :arrow_up: 升级 `swagger-bootstrap-ui` 到 `1.9.4`.

### v1.1.2 - 2019-06-05
- :sparkles: 添加 `mica-plus-swagger` 组件，改用 `swagger-bootstrap-ui`。
- :sparkles: 项目启动完成之后停止控制台日志打印。
- :zap: 优化 `MicaBeanCopier` 避免 copy 代码未改包造成的出错。
- :zap: `ObjectUtil` 添加 isNull、isNotNull、isTrue、isFalse 等方法并同步进 `$`。
- :zap: `BaseController` 改为 `IController` 方便业务中进行二次扩展。
- :sparkles: 添加 `lbWebClient` 和 `header` 透传处理功能，`Spring cloud gateway` 中用起来特`爽`。
- :sparkles: `LauncherService` 支持 ordered 排序.
- :zap: 优化 redis 组件，
- :zap: 启动监听，如果有 swagger，打印 swagger ui 地址。
- :zap:  调整验证码旋转度数，避免文字遮挡。
- :zap:  优化异常处理.
- :zap:  添加只有 message 的 ServiceException 构造器.
- :memo: 添加 snapshots 版本，snapshots 版本会及时响应，修复最新的 bug 或者必要的需求。.
- :bug: gitee #IWLLH (MicaHttpHeadersContextHolder 只支持 Servlet) 优化避免 webflux 报错。
- :bug: 修复 ip 获取的 bug。
- :arrow_up: 升级 gradle 到 5.4.1.
- :arrow_up: 升级 Spring boot 到 2.1.5.RELEASE
- :arrow_up: 升级 mica-auto 到 1.1.0

### v1.1.1 - 2019-05-11
- :zap: 减少部分阿里巴巴规范问题。
- :zap: 优化日志，dev 环境日志，不按内存切分，不使用 gz 压缩，避免每次本地重启生成日志文件。
- :zap: 优化 StackTraceAsString 中 FastStringWriter 初始容量为 200。
- :zap: 优化 ServiceException R 泛型。
- :sparkles: 添加 R.throw 系列方法，用于处理异常直接返回的情况。
- :zap: Try 添加 Runnable、Callable、Comparator 的 Lambda 受检异常处理。
- :bug: 修复日期添加和减少的 bug IW2IM。

### v1.1.0 - 2019-04-26
- :zap: 将 mica 版本写入 banner.txt。
- :mute: nacos 日志影响了 gateway 和 webflux 的日志，调高级别。
- :zap: 优化 Servlet 和 Webflux 请求日志打印效果。
- :sparkles: 添加部分工具类。
- :sparkles: 优化验证码生成。
- :sparkles: 开源所有 `mica-pro` 功能。
- :sparkles: `mica-pro` 中的 `http-cache` 注解部分移入 `mica-boot`（暂时只支持 Servlet）。
- :sparkles: 开源 `mica-cloud` 模块。
- :sparkles: 开源 `mica-plus-error-catch` 模块。
- :sparkles: 开源 `mica-plus-redis` 模块。
- :sparkles: 开源 `mica-plus-mongo` 模块。
- :sparkles: 添加 Validated Get、Create、Update、DeleteGroup。

### v1.0.1 - 2019-04-03
- :ok_hand: 处理几处 P3C 代码检查问题。@冷冷
- :zap: 优化泛型，避免部分环境下的编译问题。
- :sparkles: 添加 lutool 中的 renderJson。
- :zap: 优化 dateUtils 性能。
- :zap: 优化 RuntimeUtil，提高性能。
- :zap: 升级 gradle 到 `5.3.1`。

### v1.0.0 - 2019-03-20
- :bug: 修复 `webflux` 下不支持的配置。
- :zap: 异常 event `requestUrl` 拼接 `queryString`，添加 `requestMethod` 参数。
- :zap: 调整环境处理和请求日志，方便动态调整。
- :zap: 调整 `base64` 验证码为直接返回 `Captcha` 对象，方便二次处理。
- :wrench: `swagger` 服务名不使用大写，`webflux swagger` 仅仅自动配置 `Docket`。
- :heavy_plus_sign: 添加 `lutool` 中的资源读取工具。
- :zap: 优化文件下载。

### v0.0.1-RC4 - 2019-03-13
- :heavy_plus_sign: webflux url 版本号和 header 版本处理。
- :heavy_plus_sign: webflux 异常统一处理，未知异常发送 Event 事件，方便监听收集。
- :heavy_plus_sign: webflux 枚举转换，规则同 jackson。
- :heavy_plus_sign: webflux RequestContextHolder，方便获取 webflux request。

### v0.0.1-RC3 - 2019-03-05
- :zap: 优化 base64 验证码。可完美结合 mica-pro redis cache name # 号分割超时。
- :loud_sound: 优化`请求日志`，避免并发下顺序错乱。
- :pushpin: 升级 `mica-auto`。
- :zap: 优化 UUID，采用 java9 的方式，提高性能。
- :heavy_plus_sign: bom 添加 `mica-pro` 依赖。
- :loud_sound: 异常事件添加触发时间。
- :pencil2: fix spelling issue about black -> blank。 `感谢：` github @xkcoding
- :zap: 优化日志，`spring boot admin` 中显示 `info` 日志。
- :zap: 升级 gradle 版本到 `5.2.1`。

### v0.0.1-RC2 - 2019-02-19
- 修复 `PathUtil` 导包问题。
- 优化 `mica props`。
- 优化 `Bean copy` 逻辑。

### v0.0.1-RC1 - 2019-01-23
#### 初始化项目
- `mica-bom` 依赖 bom。
- `mica-core` mica 核心工具集。
- `mica-captcha` mica 验证码。
- `mica-launcher` mica 启动器。
- `mica-log4j2` log4j2 配置。
- `mica-boot` spring boot 扩展。
- `mica-boot-test` 更加方便测试。
