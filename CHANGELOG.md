# 变更记录

## 发行版本
### [1.0.0] - 2019-03-20
- :bug: 修复 `webflux` 下不支持的配置。
- :zap: 异常 event `requestUrl` 拼接 `queryString`，添加 `requestMethod` 参数。
- :zap: 调整环境处理和请求日志，方便动态调整。
- :zap: 调整 `base64` 验证码为直接返回 `Captcha` 对象，方便二次处理。
- :wrench: `swagger` 服务名不使用大写，`webflux swagger` 仅仅自动配置 `Docket`。
- :heavy_plus_sign: 添加 `lutool` 中的资源读取工具。
- :zap: 优化文件下载。

### [0.0.1-RC4] - 2019-03-13
- :heavy_plus_sign: webflux url 版本号和 header版本处理。
- :heavy_plus_sign: webflux 异常统一处理，未知异常发送 Event 事件，方便监听收集。
- :heavy_plus_sign: webflux 枚举转换，规则同 jackson。
- :heavy_plus_sign: webflux RequestContextHolder，方便获取 webflux request。

### [0.0.1-RC3] - 2019-03-05
- :zap: 优化 base64 验证码。可完美结合 mica-pro redis cache name # 号分割超时。
- :loud_sound: 优化`请求日志`，避免并发下顺序错乱。
- :pushpin: 升级 `mica-auto`。
- :zap: 优化UUID，采用 java9的方式，提高性能。
- :heavy_plus_sign: bom 添加 `mica-pro` 依赖。
- :loud_sound: 异常事件添加触发时间。
- :pencil2: fix spelling issue about black -> blank。 `感谢：` github @xkcoding
- :zap: 优化日志，`spring boot admin` 中显示 `info` 日志。
- :zap: 升级 gradle 版本到 `5.2.1`。

### [0.0.1-RC2] - 2019-02-19
- 修复 `PathUtil` 导包问题。
- 优化 `mica props`。
- 优化 `Bean copy` 逻辑。

### [0.0.1-RC1] - 2019-01-23
#### 初始化项目
- `mica-bom` 依赖 bom。
- `mica-core` mica 核心工具集。
- `mica-captcha` mica 验证码。
- `mica-launcher` mica 启动器。
- `mica-log4j2` log4j2 配置。
- `mica-boot` spring boot 扩展。
- `mica-boot-test` 更加方便测试。
