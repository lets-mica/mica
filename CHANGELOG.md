# 变更记录

## 发行版本

### v3.4.3 - 2025-02-23

- :sparkles: mica-metrics UndertowMetrics 不再需要反射
- :sparkles: 折腾不动了，删除 gradle 支持
- :bug: mica-redis 修复redis配置顺序问题
- :bug: mica-core DesensitizationUtil 修复 .StringIndexOutOfBounds
- :bug: mica-core 解决 bean copy java17 反射报错

### v3.2.5 - 2024-04-26

- :sparkles: mica-core 内部直接使用 StandardCharsets
- :sparkles: mica-core 添加一个 id 生成系列方法，原理同 uuid
- :sparkles: mica-redis !34 fix(RedisSerializer): 适配kotlin类型 (data class 是 final修饰的)
- :sparkles: mica-lite refactor(MicaExceptionTranslator): change log position

### v3.2.4 - 2024-03-23

- :sparkles: mica-redis 修复redis启动警告 gitee #I8REGD #32 感谢 `@mymx2`
- :sparkles: mica-xss 调整逻辑 gitee #I9AL2N 感谢 `@okayaok` 反馈
- :sparkles: mica-core validation 增加范围验证 gitee #30 感谢 `@mymx2`
- :sparkles: mica-core 完善 HexUtil
- :sparkles: mica-lite 区分knife4j路径 gitee #28 感谢 `@mymx2`
- :sparkles: mica-lite 完善 FieldError 为空问题 gitee #27 感谢 `@mymx2`

### v3.2.3 - 2024-03-02
- :sparkles: mica-ip2region 升级 ip2region xdb 文件。
- :sparkles: mica-http 简化 ssl 配置。
- :arrow_up: 依赖升级

### v3.2.2 - 2024-01-21
- :sparkles: mica-holidays graalvm AOT 支持。
- :bug: mica-ip2region 修复 ipv6 数据库文件 graalvm AOT 支持。感谢 github `@lishangbu` pr
- :sparkles: 依赖升级

### v3.2.0 - 2023-12-02

- :sparkles: mica-http 完善和优化，使用控制台日志时，可以没有 slf4j 依赖。
- :sparkles: mica-core 完善 JsonUtil。
- :arrow_up: 依赖升级

### v3.1.6 - 2023-12-02

- :sparkles: mica-http 完善和优化，使用控制台日志时，可以没有 slf4j 依赖。
- :sparkles: mica-core 完善 JsonUtil。
- :arrow_up: 依赖升级

### v3.1.5.1 - 2023-10-28

- :sparkles: mica-holidays 模块，更新 2024 节假日。

### v3.1.5 - 2023-10-21

- :sparkles: 添加 mica-holidays 模块
- :sparkles: 完善 mica-http 感谢好友 `@吴天狗` 反馈
- :sparkles: mica-core RsaUtil 支持分段 gitee #I855S0 感谢 `@桂在坚持` 反馈
- :sparkles: 升级 gradle wrapper 版本到 8.4
- :sparkles: 升级 mica-auto 版本

### v3.1.3 - 2023-08-27

- :sparkles: 完善 mica-lite 支持 JsonView 和 R 统一封装返回 github #53 感谢 `@yangwenliang123` 反馈。
- :sparkles: 优化 mica-core 完善、优化 uuid
- :sparkles: 优化 mica-core 完善 JsonUtil，添加 toJsonWithView 系列方法。
- :sparkles: 添加 mica-nats 模块，高性能、轻量级 mq。
- :sparkles: 优化 mica-ip2region 默认为 utf-8 字符集 gitee #I7TT74 感谢 `@Hi,all`。
- :sparkles: 完善 mica-redis 方便 geo、HyperLogLog 操作。

### v3.1.1 - 2023-07-16
- :sparkles: mica-redis key 支持统一添加前缀和自定义 key 处理
- :sparkles: mica-redis 限流时间使用毫秒数
- :arrow_up: 升级到 Spring boot 3.1.1

### v3.0.8 - 2023-06-24
- :sparkles: mica-ip2region 升级 `ip2region.xdb`
- :sparkles: mica-redis pub、sub 应该使用 `RedisMessageListenerContainer` 进行订阅
- :sparkles: mica-redis 优化 `RPubSubEvent`

### v3.0.7 - 2023-05-20
- :sparkles: mica-redis 完善 MicaRedisCache，添加 `setByJsonSer` 方法。
- :bug: mica-core 修复 NumberUtil form62Str 转回不准的问题，感谢 `@Just do it` 反馈。

### v3.0.6 - 2023-04-23
- :sparkles: mica-redis 添加对 pub sub 的简单封装
- :sparkles: mica-ip2region 支持 ipv6
- :sparkles: mica-http 优化
- :bug: mica-xss 修复 `@XssCleanIgnore` 注解判断

### v3.0.5 - 2023-03-25
- :sparkles: 完善 HexUtil 工具类
- :arrow_up: 依赖升级，升级到安全版的 Spring boot。

### v3.0.4 - 2023-03-01
- :arrow_up: 升级到 Spring boot 3.0.4

### v3.0.3 - 2023-03-01
- :sparkles: 优化 `Base64Util`
- :sparkles: 使用 `@AutoConfiguration` 优化代码
- :hammer: 添加发版脚本，方便发布到中央库
- :arrow_up: 依赖升级

### v3.0.1 - 2022-12-23
- :sparkles: 代码优化，升级到高版本 jdk 语法
- :sparkles: 优化 gradle 参数，开启缓存和并行处理
- :sparkles: gradle 版本控制重构，感谢 `@livk-cloud` pr
- :arrow_up: 升级到 Spring boot 3.0.1
- :arrow_up: 升级到 Spring cloud 2022.0.0

### v3.0.0 - 2022-12-11
- :sparkles: gradle 添加 `-parameters` 编译参数
- :sparkles: 部分模块扩展 RuntimeHints 支持 AOT
- :sparkles: mica-ip2region 更新 `ip2region.xdb`
- :sparkles: 代码优化，升级到高版本 jdk 语法
- :arrow_up: 升级到 java17
- :arrow_up: 升级到 Spring boot3

### v2.7.17.1 - 2023-10-28

- :sparkles: mica-holidays 模块，更新 2024 节假日。

### v2.7.17 - 2023-10-21

- :sparkles: 添加 mica-holidays 模块
- :sparkles: 完善 mica-http 感谢好友 `@吴天狗` 反馈
- :sparkles: mica-core RsaUtil 支持分段 gitee #I855S0 感谢 `@桂在坚持` 反馈
- :sparkles: 升级 gradle wrapper 版本到 8.4
- :sparkles: 升级 mica-auto 版本

### v2.7.15 - 2023-08-27

- :sparkles: 完善 mica-lite 支持 JsonView 和 R 统一封装返回 github #53 感谢 `@yangwenliang123` 反馈。
- :sparkles: 优化 mica-core 完善、优化 uuid
- :sparkles: 优化 mica-core 完善 JsonUtil，添加 toJsonWithView 系列方法。
- :sparkles: 添加 mica-nats 模块，高性能、轻量级 mq。
- :sparkles: 优化 mica-ip2region 默认为 utf-8 字符集 gitee #I7TT74 感谢 `@Hi,all`。
- :sparkles: 完善 mica-redis 方便 geo、HyperLogLog 操作。

### v2.7.14 - 2023-07-25
- :sparkles: mica-redis key 支持统一添加前缀和自定义 key 处理
- :sparkles: mica-redis 限流时间使用毫秒数
- :arrow_up: 升级到 Spring boot 2.7.14

### v2.7.13 - 2023-06-24
- :sparkles: mica-ip2region 升级 `ip2region.xdb`
- :sparkles: mica-redis pub、sub 应该使用 `RedisMessageListenerContainer` 进行订阅
- :sparkles: mica-redis 优化 `RPubSubEvent`

### v2.7.12 - 2023-05-20
- :sparkles: mica-redis 完善 MicaRedisCache，添加 `setByJsonSer` 方法。
- :bug: mica-core 修复 NumberUtil form62Str 转回不准的问题，感谢 `@Just do it` 反馈。

### v2.7.11 - 2023-04-23
- :sparkles: mica-redis 添加对 pub sub 的简单封装
- :sparkles: mica-ip2region 支持 ipv6
- :sparkles: mica-http 优化
- :bug: mica-xss 修复 `@XssCleanIgnore` 注解判断

### v2.7.10 - 2023-03-23
- :sparkles: 优化 Base64Util
- :sparkles: 完善 HexUtil 工具类
- :arrow_up: 依赖升级

### v2.7.9 - 2023-02-24
- :sparkles: mica-ip2region 更新 ip2region.xdb
- :sparkles: 添加发布脚本
- :sparkles: 优化 gradle 参数，开启并发。
- :sparkles: 使用 `@AutoConfiguration` 优化代码。
- :arrow_up: 依赖升级

### v2.7.6 - 2022-11-28
- :sparkles: mica-core NumberUtil 调整 62 进制转十进制
- :sparkles: mica-redis 添加 redis set 带自定义序列化方法，方便使用
- :bug: mica-metrics 使用 HikariCP 并且有 druid jar 时报错问题，修复 #I60O8C
- :arrow_up: 依赖升级

### v2.7.5 - 2022-10-21
- :sparkles: mica-xss `@XssCleanIgnore` 注解添加 `value()` 数组，支持忽略 json body 字段名。感谢 `冷冷（pig开源）`提出的需求。
- :sparkles: mica-redis `hash` 便捷取并集，感谢 `@just-cloud (zqf)` pr
- :bug: mica-xss json int、bool 等基础类型映射到字符串时的类型问题。感谢 `@有请岁月` 反馈。
- :arrow_up: 依赖升级

### v2.7.4 - 2022-09-25
- :sparkles: mica-redis 方便多 redis 数据源使用。
- :sparkles: mica-xss 优化异常提示。
- :sparkles: mica-xss 增加校验 mode，提示异常
- :sparkles: 升级使用 junit5
- :sparkles: 开源之夏 mica-openapi 完成 #I5539K
- :memo: 描述错误，感谢 `@iacker` pr
- :bug: mica-redis 修复 lPush list 参数问题。
- :arrow_up: 依赖升级

### v2.7.3 - 2022-08-21
- :sparkles: mica-core 统一去除 @UtilityClass，方便继承。
- :sparkles: mica-xss 放开 XssHolder，方便自定义处理。
- :sparkles: ObjectProvider<List> 统一替换成 ObjectProvider orderedStream() [#14467](https://github.com/spring-projects/spring-boot/issues/14467) 
- :sparkles: mica-redis Jackson 改为 JsonUtil.getInstance() 避免受 web 影响。
- :sparkles: mica-redis 添加对 bitmap 支持。感谢 `@BlackR` 贡献。
- :sparkles: mica-redis 限流 lua 脚本优化，更好支持阿里 redis，感谢 `@火炎焱燚` 反馈。
- :bug: mica-redis 修复设置默认缓存名分割符号为 “:” 没生效。
- :arrow_up: 升级 okhttp 到 4.x

### v2.7.2 - 2022-07-24
- :sparkles: mica-core JsonUtil 添加 toPrettyJson 感谢 `@老古` 反馈。
- :sparkles: mica-core JsonUtil 添加 getType。
- :sparkles: mica-redis 设置默认缓存名分割符号为 “:”。
- :sparkles: mica-redis stream add 方法完善，方便限制 stream 长度。
- :sparkles: mica-redis incrBy, decrBy新增Duration过期时间单位，感谢 `@BlackR` pr。
- :sparkles: update mica-spider/README.md. 感谢 `@BlackR` pr
- :bug: 修复 git 修改 xdb 文件，导致单元测试失败。
- :arrow_up: 依赖升级

### v2.7.1.1 - 2022-06-27
- :sparkles: 升级 ip2region

### v2.7.1.0 - 2022-06-25
- :sparkles: 更新 mica-ip2region db文件，感谢 `@lishangbu`
- :sparkles: mica-core SpringContextUtil 添加支持Object类型的事件发布，感谢 `@lishangbu`
- :bug: mica-redis 修复 2.7.x 下 CacheManager 不生效的问题。

### v2.7.1 - 2022-06-24
- :sparkles: mica-redis 添加 pub、sub 方便使用。
- :sparkles: mica-redis setNX 添加超时时间支持，感谢 github `@lay-g`
- :sparkles: mica-redis 添加 setnx 命令支持，感谢 github `@lay-g`
- :sparkles: mica-xss 完善，数据类型不符合时抛出异常，github #46
- :sparkles: mica-core 添加只包含数字和字母的 NanoId StringUtil#getNanoId62()
- :sparkles: mica-core 完善 AesUtil，添加同 mysql 通用的加密解密。
- :sparkles: mica-core 添加 http 解析，规则同 idea http。
- :sparkles: mica-core 完善 JsonUtil。
- :memo: 添加 mica-xss 不生效排查步骤。
- :arrow_up: spring boot 到 2.7.1

### v2.6.8 - 2022-05-22
- :sparkles: mica-logging 支持自定义 Logstash 字段配置。
- :sparkles: mica-activerecord SqlLogFilter 支持正则表达式过滤要打印的 sql。
- :sparkles: mica-core Pair 支持 jackson 序列化。
- :arrow_up: Upgrading dependencies.

### v2.6.7 - 2022-04-30
- :sparkles: mica-logging 升级 logstash-logback-encoder 代码调整。
- :sparkles: mica-logging 升级 loki-logback-appender 代码调整。
- :sparkles: mica-core 添加 StreamUtil
- :sparkles: mica-redis stream 添加 bytes 方便自定义处理
- :memo: 完善 mica-redis stream 使用文档。
- :arrow_up: spring boot 到 2.6.7
- :arrow_up: spring cloud 到 2021.0.2

### v2.6.6 - 2022-04-02
- :sparkles: mica-prometheus 添加 profile 标签，用于多环境。
- :sparkles: mica-redis 添加 stream，默认关闭。
- :sparkles: mica-core 完善 DigestUtil。
- :sparkles: mica-core StringUtil 添加 getNanoId 21位的唯一Id，基于 java9+ uuid 算法，性能更加强悍。
- :sparkles: 添加 mica-openapi 模块。
- :arrow_up: spring boot 到 2.6.6

### v2.5.8 - 2021-12-30
- :sparkles: mica-xss 添加一个使用场景。
- :sparkles: JsonUtil 新增支持 java.io.Reader 转换操作 感谢 @caiqyxyx
- :memo: fix Comment error close #I4K6LZ
- :arrow_up: spring boot 到 2.5.8
- :arrow_up: spring cloud 到 2020.0.5
- :arrow_up: jetCache 到 2.6.2

### v2.5.7 - 2021-11-28
- :sparkles: mica-redis 优化，方便自定义序列化。
- :sparkles: mica-xss 优化，避免 xss 关闭时被类扫描，导致 bean 找不到异常。
- :sparkles: mica-core 添加 retry 接口。
- :sparkles: mica-http 代码优化去掉 spring retry 依赖。
- :memo: mica-redis 优化 README.md 文档。
- :memo: mica-http 更新 README.md 文档。
- :arrow_up: Upgrading dependencies.
- :arrow_up: 升级 Gradle 到 7.3。
- :arrow_up: 升级 lombok 到 1.18.22。
- :arrow_up: 升级 Spring boot 到 2.5.7。
- :arrow_up: 升级 mica-weixin 到 2.1.2。

### v2.5.6 - 2021-10-28
- :sparkles: mica-redis 优化 MicaRedisCache bean 名称。
- :bug: mica-redis rpush、lpush 修复，优化 MicaRedisCache 方法泛型。
- :sparkles: mica-spider 更好的支持 java17
- :sparkles: mica-core BeanUtil#generator 支持 java17
- :arrow_up: 升级 druid 到 1.2.8
- :arrow_up: 升级到 Spring boot 到 2.5.6

### v2.5.5 - 2021-09-25
- :sparkles: mica-http 添加 HttpException 方便异常时获取相应信息。
- :sparkles: mica-http ResponseSpec 添加 isNotOk 用于重试断言。
- :sparkles: mica-core 优化 bean copy 和 bean map 支持 java17。
- :sparkles: mica-core 优化 Mica Context 接口。
- :sparkles: 优化 github action。
- :sparkles: 升级 gradle 版本到 7.2，支持 java17。
- :bug: mica-qrcode 修复 toImage 字符集问题。
- :bug: mica-xss 目前只支持 servlet 只在 servlet 下启用。
- :bug: mica-core 修复 ObjectUtil#toBoolean 方法，感谢 `@caiqyxyx` 同学反馈。
- :arrow_up: 升级到 Spring boot 到 2.5.5
- :arrow_up: 升级到 Spring cloud 2020.0.4。

### v2.4.11 - 2021-09-25
- :sparkles: mica-http 添加 HttpException 方便异常时获取相应信息。
- :sparkles: mica-http ResponseSpec 添加 isNotOk 用于重试断言。
- :sparkles: mica-core 优化 bean copy 和 bean map 支持 java17。
- :sparkles: mica-core 优化 Mica Context 接口。
- :sparkles: 优化 github action。
- :sparkles: 升级 gradle 版本到 7.2，支持 java17。
- :bug: mica-qrcode 修复 toImage 字符集问题。
- :bug: mica-xss 目前只支持 servlet 只在 servlet 下启用。
- :bug: mica-core 修复 ObjectUtil#toBoolean 方法，感谢 `@caiqyxyx` 同学反馈。
- :arrow_up: 升级到 Spring boot 到 2.4.11
- :arrow_up: 升级到 Spring cloud 2020.0.4。

### v2.5.4 - 2021-08-22
- :sparkles: 新增 mica-prometheus 模块支持 http sd 和 alert webhook。
- :sparkles: mica-swagger 支持 v3 注解 `@Tag`，`R` 添加 v3 注解。  
- :sparkles: mica-logging 代码优化。
- :sparkles: github actions 添加缓存。
- :memo: 更新模块图。
- :memo: [Summer 2021] 添加英文 readme。
- :arrow_up: 升级 Spring Native 到 0.10.2。
- :arrow_up: 升级到 Spring boot 到 2.5.4
- :arrow_up: 升级 mica-auto 到 2.1.3 修复多模块增量编译问题。
- :arrow_up: 升级 jsoup 到 1.14.2，不再支持低版本 jsoup。
- :arrow_up: 升级 knife4j 到 3.0.3
- :arrow_up: 升级到 jfinal 到 4.9.16

### v2.5.3 - 2021-07-30
- :sparkles: 完善 mica-lite，添加 SpringContextUtil bean，文件上传添加开关。
- :arrow_up: 升级到 jfinal 到 4.9.15
- :arrow_up: 升级到 jsoup 到 1.14.1
- :arrow_up: 升级到 mica-auto 到 2.1.2

### v2.5.2 - 2021-07-02
- :sparkles: 代码优化，减少 codacy 上的问题。
- :sparkles: 优化 mica-http 添加 cookie 管理。
- :memo: 完善 mica-http README.md。
- :memo: 更新模块图。
- :arrow_up: 升级到 Spring boot 到 2.5.2
- :arrow_up: 升级到 Spring Native 到 0.10.1
- :arrow_up: 升级到 mica auto 到 2.1.1

### v2.4.8 - 2021-07-02
- :sparkles: 代码优化，减少 codacy 上的问题。
- :sparkles: 优化 mica-http 添加 cookie 管理。
- :memo: 完善 mica-http README.md。
- :memo: 更新模块图。
- :arrow_up: 升级到 Spring boot 到 2.4.8
- :arrow_up: 升级到 Spring Native 到 0.10.1
- :arrow_up: 升级到 mica auto 到 2.1.1

### v2.5.1 - 2021-06-15
- :sparkles: 整体代码优化减少 codacy 上的问题。
- :sparkles: mica-http 代码优化，支持自定义 Logger。
- :sparkles: 添加 mica-activerecord 模块。
- :sparkles: mica-core 优化 DisableValidationTrustManager 方便使用。
- :sparkles: mica-redis 可自定义 RedisTemplate。
- :sparkles: mica-logging loki sender 默认值优化，根据依赖判断，gitee #I3T68Y。
- :arrow_up: 升级到 Spring cloud 2020.0.3。
- :arrow_up: 升级到 Spring boot 到 2.5.1。

### v2.4.7 - 2021-06-15
- :sparkles: 整体代码优化减少 codacy 上的问题。
- :sparkles: mica-http 代码优化，支持自定义 Logger。
- :sparkles: 添加 mica-activerecord 模块。
- :sparkles: mica-core 优化 DisableValidationTrustManager 方便使用。
- :sparkles: mica-redis 可自定义 RedisTemplate。
- :sparkles: mica-logging loki sender 默认值优化，根据依赖判断，gitee #I3T68Y。
- :arrow_up: 升级到 Spring cloud 2020.0.3。
- :arrow_up: 升级到 Spring boot 到 2.4.7。

### v2.5.0.1 - 2021-05-25
- :sparkles: 代码优化，减少 codacy 上的问题。
- :bug: 修复 mica-redis RedisTemplate 初始化。
- :memo: update README.md 更新文档地址。
- :sparkles: 优化包导入。

### v2.4.6.1 - 2021-05-25
- :sparkles: 代码优化，减少 codacy 上的问题。
- :bug: 修复 mica-redis RedisTemplate 初始化。
- :memo: update README.md 更新文档地址。
- :sparkles: 优化包导入。

### v2.5.0 - 2021-05-23
- :sparkles: mica-redis 微调，支持 Spring boot 到 2.5.0。
- :arrow_up: 升级 Spring boot 到 2.5.0。

### v2.4.6 - 2021-05-23
- :sparkles: mica-logging 完成 loki 支持 #36 #I3PX2F。
- :sparkles: mica-ip2region、mica-captcha 添加对 spring-native 的支持 #38 #I3PX2N。
- :sparkles: mica-jetcache 添加 metrics 支持 #37 #I3PX2K。
- :sparkles: mica-caffeine 添加不支持自定义 Caffeine bean 提示。
- :sparkles: mica-core R 添加 throwOn 系列方法。
- :sparkles: mica-redis 优化 ICacheKey 和 scan。
- :sparkles: 代码统一优化，减少部分阿里巴巴规约提示。
- :bug: mica-logging 修复 LoggingInitializer Spring boot 2.4.x 失效的问题。
- :arrow_up: 升级 druid 到 1.2.6。
- :arrow_up: 升级 Spring boot 到 2.4.6。

### v2.4.5 - 2021-04-28
- :sparkles: 添加 mica-jetcache 模块。
- :sparkles: 添加 mica-lite 模块。
- :sparkles: mica-metrics 重构 UndertowMetrics，暴露更加有用的指标。
- :sparkles: mica-metrics 完善 DruidMetrics，暴露更加有用的指标。
- :sparkles: mica-redis 调整 bean 名称 redisTemplate 为 micaRedisTemplate 减少冲突。
- :sparkles: mica-captcha 中的 cache 改为每次读取， caffeine 会刷新，照成引用为 null。
- :sparkles: mica-captcha 优化 bean 名称和添加 generateBase64Vo 方法。
- :sparkles: mica-logging 减少 reflections 日志，readme 添加阿里云、腾讯云日志服务接入链接。
- :sparkles: mica-qrcode 添加 base64 image 方法。
- :sparkles: mica-core 添加网关通用 code。
- :sparkles: mica-core 添加 CollectionUtil computeIfAbsent 方法 避免 jdk8 下的 bugs JDK-8161372
- :sparkles: mica-core Pkcs7Encoder 中默认的 BLOCK_SIZE 改为 16 github #35 兼容更多编程语言。
- :bug: mica-caffeine 多 cache name 时报错。
- :arrow_up: 升级 spring boot 到 2.4.5
- :arrow_up: 升级 mica-weixin 到 2.1.0（优化对 mica-caffeine 的支持）

### v2.4.4-GA - 2021-03-28
- :sparkles: mica-qrcode 新增模块，友好的二维码识别和生成
- :sparkles: mica-logging 重新设计，`logstash-logback-encoder` 调整为可选，`logstash` 和 `json` 需手动添加依赖
- :sparkles: mica-core 优化完善 DesensitizationUtil
- :sparkles: mica-core 添加 ImageUtil
- :sparkles: mica-ip2region 更新 db 文件 gitee #I3AJNV
- :bug: mica-redis 修复 ScanOptions count 空指针
- :arrow_up: 升级到 mica-auto 到 2.0.4
- :arrow_up: 升级到 mica-weixin 到 2.0.6
- :arrow_up: 升级到 spring cloud 2020.0.2
- :arrow_up: 升级 spring boot 到 2.4.4

### v2.4.3-GA - 2021-02-27
- :sparkles: 开源 mica-logging 组件。
- :sparkles: mica-core 完善 JsonUtil 和 SystemUtil。
- :sparkles: mica-core 请求开始时间 key。
- :sparkles: mica-xss 支持转义和清理2种模式。
- :sparkles: mica-swagger api key 认证 token key-name 默认改为 `Authorization`。
- :memo: 添加观星图。
- :memo: 更换模块图。
- :arrow_up: 升级 spring boot 到 2.4.3

### v2.4.2-GA - 2021-02-08
- :sparkles: mica-caffeine、mica-redis 默认 @EnableCaching。
- :sparkles: mica-bom 添加 mica-caffeine 模块。

### v2.4.1-GA - 2021-01-29
- :bug: mica-xss ThreadLocal remove。
- :sparkles: mica-xss 完善，新增字符串全局 trim、换行、转义配置。

### v2.4.0-GA - 2021-01-18
- :sparkles: mica-xss XssUtil 放开 Whitelist 方便自定义。
- :sparkles: mica-core 添加 ACTIVE_PROFILES_PROPERTY 常量。
- :sparkles: 移动 SpringContextUtil 到 mica-core 中。
- :arrow_up: 升级到 spring boot 2.4.2。
- :arrow_up: 使用 spring cloud 2020.0.0
- :arrow_up: 升级 mica auto 到 2.0.3。
- :arrow_up: 升级到 mica-weixin 2.0.5。

### v2.1.1-GA - 2021-02-22
- :bug: mica-xss ThreadLocal remove。
- :arrow_up: 升级到 mica-weixin 2.0.5。

### v2.1.0-GA - 2020-12-13
- :sparkles: mica-bom 添加 mica-weixin。
- :sparkles: mica-redis 添加 redis key 失效事件配置。
- :sparkles: mica-swagger 添加 SwaggerCustomizer，用于自定义配置。
- :sparkles: mica-swagger 添加 oauth2 认证配置。
- :sparkles: mica-http 添加流上传，完善文档。
- :sparkles: mica-core 添加 GeoUtil。
- :sparkles: mica-core StringUtil 优化 format 方法参数泛型。
- :sparkles: mica-core JsonUtil 添加参数化类型 getParametricType 方法。
- :arrow_up: 升级 knife4j 到 3.0.2。
- :arrow_up: 升级 druid 到 1.2.3。
- :arrow_up: 升级 spring boot 到 2.3.7.RELEASE。

### v2.0.9-GA - 2020-11-15
- :sparkles: mica-xss 可自定义 XssCleaner 接口。
- :sparkles: mica-xss 日志统一调整为 debug。
- :sparkles: mica-xss 默认拦截 /** #I24753 。
- :sparkles: mica-ip2region 更新 db 文件。
- :sparkles: mica-redis 优化 redis scan。
- :sparkles: mica-redis 添加强制依赖 commons-pool2。
- :arrow_up: mica-bom 添加 druid 到 bom。
- :sparkles: mica-core 完善 ReflectUtil。
- :arrow_up: 升级 spring cloud 到 Hoxton.SR9。
- :arrow_up: 升级 spring boot 到 2.3.6.RELEASE。

### v2.0.8-GA - 2020-10-20
- :sparkles: mica-metrics 添加对 druid 连接池的支持。
- :sparkles: mica-core StringUtil 添加格式化执行时间方法。
- :sparkles: mica-core CollectionUtil 优化泛型。
- :sparkles: mica-core 完善 FileUtil，方便使用
- :sparkles: mica-core 完善 JsonUtil，方便使用。
- :sparkles: mica-core 完善 DateUtil，方便使用。
- :sparkles: mica-core 抽取 Pkcs7Encoder。
- :sparkles: mica-core Validated group extends Default.
- :memo: 修复文档错误.
- :memo: 添加 mica 生态连接。

### v2.0.7-GA - 2020-09-25
- :memo: 文档完善，详见：http://wiki.dreamlu.net
- :sparkles: mica-core Once 添加方法。
- :sparkles: 优化 mica-jobs 模块，默认开启。
- :sparkles: 添加 DesensitizationUtil 脱敏工具类。
- :sparkles: 添加 commons-lang 中的字符串系列操作方法。
- :sparkles: 优化 mica-redis getCounter。
- :sparkles: mica-redis 优化 incrBy、decrBy，添加带超时的方法。
- :sparkles: mica-redis 优化 jdk 序列化。
- :arrow_up: Upgrading dependencies. spring boot 2.3.4
- :arrow_up: 升级到 Spring cloud Hoxton.SR8。

### v2.0.6-GA - 2020-08-22
- :sparkles: mica-core BeanUtil 添加 toNewMap 方法。
- :sparkles: 优化 mica-core MicaConstant 中添加 mdc accountId。
- :sparkles: 优化 mica-core 补上 LuTool 中的 Bean diff。
- :sparkles: 优化 mica-core DateUtil 添加部分方法。
- :sparkles: mica-captcha graphics dispose.
- :sparkles: mica-http 文档添加https证书示例。
- :arrow_up: 升级 spring boot 到 2.3.3.RELEASE。

### v2.0.5-GA - 2020-08-09
- :sparkles: 添加 mica-metrics 模块，用于使用 prometheus 进行指标收集，已支持 undertow、sentinel。
- :sparkles: mica-redis add scan 和 sscan。
- :memo: mica-redis 修复使用文档。 Gitee pr by @醉酒的蝴蝶zzz/N/A
- :sparkles: mica-core 添加 RsaHelper RSA PEM格式秘钥对的解析和导出，Gitee pr by @caiqiyuan
- :sparkles: mica-core RsaUtil 完善加解密方法。
- :sparkles: mica-core 添加忽略序列化 id 的 jdk 对象序列化。
- :sparkles: mica-core 添加 CheckedPredicate。
- :sparkles: mica-core 添加 json 格式校验。
- :bug: 修复部分 sonarcloud 问题。
- :arrow_up: 升级 spring cloud 到 Hoxton.SR7。

### v2.0.4-GA - 2020-07-25
- :memo: 完善文档.
- :sparkles: mica-http 添加 pathParam 方法。
- :sparkles: mica-ip2region IpInfo 添加 getAddress 方法。
- :sparkles: 添加 mica-xss 模块.
- :sparkles: mica-core BeanUtil add deepClone method.
- :sparkles: mica-http 使用 CompletableFuture 优化异步 github #29 。
- :sparkles: mica-core INetUtil 添加内网 ip 判断。
- :sparkles: mica-spider 修复 readme，简化使用。
- :arrow_up: 升级 spring cloud 到 Hoxton.SR6。
- :arrow_up: 升级 spring boot 到 2.3.2.RELEASE。
- :arrow_up: 升级 knife4j 到 2.0.4。
- :arrow_up: 升级 mica-auto 到 1.2.5。

### v2.0.3-GA - 2020-06-25
- :memo: 完善 mica 2.x 使用文档.
- :sparkles: mica-http 微调日志，添加 system out log.
- :sparkles: mica-http 添加 asString(Charset charset) 方法。
- :sparkles: mica-http ResponseSpec 添加 json path 系列方法。
- :sparkles: mica-http 优化 github #26 .
- :sparkles: mica-ip2region 更新 ip2region.db 文件。
- :sparkles: mica-core 优化 StringUtil，添加 startWith、endWith 方法.
- :sparkles: mica-core 优化 DateUtil 优化 minus、plus months、years 提升5倍性能 gitee #IW2IM.
- :sparkles: mica-redis 优化 MicaRedisCache 构造器 github #27.
- :arrow_up: 升级 spring boot 到 2.3.1.RELEASE。

### v2.0.2-GA - 2020-06-06
- :sparkles: `release` 版本号格式为 `x.x.x-GA`，响应 `冷神` 的吐槽。
- :bug: Fixing DateUtil.parse github #25。
- :memo: mica-core 完善 javadoc，方便生成文档。
- :sparkles: mica-core Once 添加函数方法。
- :sparkles: mica-core 优化 WebUtil renderJson。
- :sparkles: mica-http Slf4jLogger 改为枚举。
- :sparkles: mica-core 添加 DecimalNum 方便金额计算。
- :sparkles: mica-core 优化 JsonUtil。
- :sparkles: 文案错误，redis prefixKeysWith 方法过时
- :sparkles: mica-captcha 添加 CaptchaVo。
- :arrow_up: spring boot to 2.3.0.RELEASE。
- :arrow_up: mica-auto version to 1.2.3。
- :arrow_up: okhttp version to 3.14.9。
- :arrow_up: update knife4j to 2.0.3。
- :arrow_up: spring cloud to Hoxton.SR5
- :wrench: Github actions publish snapshot.

### v2.0.1.20200510
- :sparkles: 开源 mica-ip2region 组件。
- :zap: mica-core 优化 R.isNotSuccess 改为 isFail。
- :bug: mica-redis jackson class info。
- :bug: mica-redis jackson findAndRegisterModules。
- :wrench: travis 替换为 Github actions，用于构建和 Snapshot 版本自动发布。
- :arrow_up: update spring boot to 2.2.7.RELEASE。

### v2.0.0 - 2020-05-04
- :sparkles: mica-http 支持指定协议 protocols。
- :zap: mica-http 扩展 HttpRequest proxy 方法，方便使用。
- :sparkles: mica-http 拆出 mica-spider 爬虫工具，精简依赖。
- :sparkles: mica-spider 增强 CssQueryMethodInterceptor。
- :zap: mica-captcha 验证码抽象,支持数学计算型验证码。
- :sparkles: mica-captcha 验证码缓存接口，完善验证码生成说明。
- :sparkles: mica-swagger 升级 swagger 到 knife4j。
- :sparkles: mica-redis 限流算法改为同 Spring cloud gateway，减少内存占用。
- :sparkles: mica-jobs 添加 xxl-job starter。
- :sparkles: mica-core 生成 mica 版本信息到 Mica 类中。
- :sparkles: mica-core 添加 compiler 工具类。
- :sparkles: mica-core 添加用于计数的 CountMap。
- :sparkles: mica-core 添加 Once 对象。
- :sparkles: mica-core 中的 jackson 包移动到 mica-boot，并做调整用于支持 mica-api-encrypt 组件。
- :sparkles: mica-core 简化 aes 工具类代码。
- :zap: mica-core 优化 DateUtil。
- :zap: mica-core 优化 RuntimeUtil。
- :sparkles: mica-core 拆解完善 HexUtil。
- :sparkles: mica-core 添加 DesUtil。
- :sparkles: mica-core 添加 RsaUtil。
- :sparkles: mica-core 优化 ResourceUtil 工具类。
- :sparkles: mica-core 微调 Bean copy。
- :sparkles: mica-core function 添加序列化。
- :sparkles: mica-core MD5 不再依赖 Spring DigestUtils。
- :sparkles: mica-core 优化 file 工具对文件名处理。
- :zap: add .gitattributes。
- :zap: @Configuration 替换为 @Configuration(proxyBeanMethods = false)。
- :arrow_up: 升级 okhttp 到 3.14.8。
- :arrow_up: 升级 jsoup 到 1.13.1。
- :arrow_up: 升级 mica-auto 到 1.2.2。
- :arrow_up: 升级 spring boot 到 2.2.6，不再支持，spring boot 2.2.x 以下版本。
- :arrow_up: 升级 spring cloud 到 Hoxton.SR4。

### v1.2.2 - 2020-03-24
- :memo: update docs.
- :bulb: 调整微信公众号
- :zap: 优化 mica-http 使 proxy 更好用 Github #18。
- :zap: 优化 mica-boot 异步异常时抛出事件监听 github @xiaopang0117
- :zap: 优化 异常日志打印。
- :zap: 优化 RequestLogAspect，区分 body 和 param。
- :bug: 修复 mica-bom 缺少 mica-actuator.
- :bug: Fixing mica-core bean copy github #14.
- :bug: Fixing mica-core JsonUtil 泛型问题.
- :bug: Fixing List Json log.
- :arrow_up: Upgrading spring boot to 2.1.13.RELEASE.
- :arrow_up: Upgrading spring cloud to Greenwich.SR5.

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
