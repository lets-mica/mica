# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概览

`mica`（云母）是一套基于 Spring Boot / Spring Cloud 的微服务开发基础组件与工具包，发布在 `net.dreamlu` group 下（`mica-bom`、`mica-core`、`mica-http` …）。项目兼容 `web`（Spring MVC）与 `webflux`，当前版本 `4.1.0`，依赖 Spring Boot `4.1.0` + Spring Cloud `2025.1.2`，JDK **17**。代码与 Javadoc 默认使用 **中文**，新增/修改工具类时请保持该约定。

## 构建、测试、发布

项目使用 **Maven** 多模块构建，`${revision}` 由根 `pom.xml` 统一管理，子模块不要硬编码版本号。`flatten-maven-plugin` 处于 `oss` 模式，发布前会自动生成 `.flattened-pom.xml`。

```bash
# 完整构建并安装到本地仓库
mvn clean install -DskipTests

# 运行所有模块的测试
mvn test

# 仅构建/测试指定模块（-am 会同时构建其依赖）
mvn -pl mica-core -am test

# 运行单个测试类
mvn -pl mica-core test -Dtest=StringUtilTest

# 运行单个测试方法
mvn -pl mica-core test -Dtest=StringUtilTest#testFormat1

# 部署到 Maven 仓库（默认 release profile，附 GPG + javadoc）
./deploy.sh                # 等价于 mvn clean package deploy -Prelease -DskipTests
./deploy.sh snapshot        # 发布 SNAPSHOT 到 sonatype
```

仓库默认 profile `develop` 使用阿里云镜像；首次构建会下载较多依赖。

## 仓库结构

```
mica-bom          依赖 BOM，对外暴露的统一版本号入口
mica-core         基础工具与扩展（utils / beans / result / context / io / http / ssl …）
mica-http         基于 OkHttp 的链式 HttpRequest/HttpResponse 客户端
mica-lite         Spring Boot 通用 starter（错误处理、Jackson、启动回调、文件上传等）
mica-openapi      springdoc / knife4j 集成
mica-spider       基于 mica-http 的爬虫/抓取工具
mica-captcha      图形/行为验证码
mica-redis        Redis 序列化、Lua、限流、发布订阅、Stream 封装
mica-ip2region    ip2region 离线库（xdb 资源已纳入 jar）
mica-xss          XSS 过滤（基于 jsoup）
mica-metrics      Micrometer/Prometheus 指标封装
mica-caffeine     Caffeine 多级缓存与 spring cache 集成
mica-logging      logback + JSON / logstash / loki appender
mica-qrcode       基于 zxing 的二维码生成与解析
mica-activerecord JFinal ActiveRecord ORM 封装
mica-prometheus   Prometheus 端点与告警封装
mica-holidays     中国节假日工具
mica-ddddocr      ddddocr（基于 onnxruntime）验证码识别
```

`mica-bom` 是其它 mica 模块版本号的统一入口，外部项目通常只需引入 `mica-bom` 即可锁定所有 mica 组件版本。

## 架构要点

- **`mica-core` 是底层基础**：几乎所有 starter（`mica-lite`、`mica-xss`、`mica-redis` …）都直接依赖它，并复用 `net.dreamlu.mica.core.utils.*`（`StringUtil`、`BeanUtil`、`CollectionUtil`、`JsonUtil`、`DateUtil` 等）。新增公共工具前先检查该包是否已有实现，避免重复。
- **工具类魔改 cglib**：`mica-core/.../beans/MicaBeanMap.java` 与 `MicaBeanCopier.java` 重写了 `org.springframework.cglib.beans.BeanMap` / `BeanCopier`，生成字节码以支持链式 Bean 与高性能拷贝，并通过 `ConcurrentMap` 缓存生成的类。这些类是 `BeanUtil.copy` 等热路径，修改时务必做 benchmark 验证。
- **`SpringContextUtil`（`mica-core/.../spring/`）**：跨模块共享的 `ApplicationContextAware` 入口，提供 `getBean`、`publishEvent`、`getCurrentProxy` 等静态方法；`mica-lite`、`mica-xss` 等模块的 `*AutoConfiguration` 都通过它拿 Bean。
- **统一响应与异常体系**：`mica-core/.../result/R.java` 是标准返回结构（`code`/`message`/`data`/`timestamp`/`traceId`），`SystemCode` 定义默认返回码。`mica-lite` 的 `MicaErrorAutoConfiguration` + `MicaErrorController` + `RestExceptionTranslator` 将 Spring MVC / WebFlux 异常统一翻译成 `R`，并通过 `MicaErrorEvent` + `MicaErrorAttributes` 暴露到 `/error`。
- **HTTP 客户端（`mica-http`）**：链式 API（`HttpRequest.get(...).addInterceptor(...).execute().asString()`），支持 JSON Pointer 拦截器、Cookie 管理、异步回调、Retry 拦截器；不依赖 Spring，可独立使用。
- **自动配置**：`mica-lite` 与多数 starter 的 `*AutoConfiguration` 通过 `mica-auto`（注解处理器，root pom 中已配置为 `annotationProcessorPaths`）生成 `spring.factories` / `AutoConfiguration.imports`，新加 starter 时不要手写 `META-INF/spring/...imports`。
- **注解处理器**：`maven-compiler-plugin` 的 `annotationProcessorPaths` 同时启用 **Lombok** 与 **mica-auto`，新模块继承父 pom 即可获得两者，无需重复配置。
- **Jackson 与序列化**：`mica-core` 使用 `tools.jackson.core:jackson-databind`（Jackson 3.x，groupId 为 `tools.jackson.core` 而非 `com.fasterxml.jackson.core`），编写 JSON 相关代码时请注意包名变化。
- **空值注解**：使用 `org.jspecify.annotations.Nullable` 标注可空参数/返回值，而非 `javax.annotation.Nullable`。
- **资源文件**：`mica-ip2region` 把 `ip2region_v4.xdb` / `ip2region_v6.xdb` 放在 `src/main/resources/ip2region/` 并随 jar 一起发布（注意 `.gitattributes` 中以 `binary` 标记），更新离线库时直接覆盖这两个文件即可。
- **Git 与忽略**：`.gitignore` 已经忽略 `.codegraph/`、`.claude/`、`.idea/`、`target/`、`*.flattened-pom.xml` 等，本地工具产物不会污染仓库。

## 代码风格

- **缩进**：`.editorconfig` 中 `*.java` 使用 **Tab**，其它文件使用空格（`*.json`/`*.yml` 为 2 空格）。行尾 LF，全 UTF-8。
- **命名**：类 `PascalCase`，方法/变量 `camelCase`，常量 `UPPER_SNAKE_CASE`。
- **Lombok**：项目大量使用 `@Data` / `@RequiredArgsConstructor` / `@Getter` / `@Slf4j`，新代码继续使用以减少样板。
- **导入**：按 `java.*` → `javax/jakarta` → `org/com` → `net.dreamlu` 分组。少量现有代码使用通配符导入，新代码建议明确导入。
- **测试**：JUnit 5（`org.junit.jupiter.api`），断言用 `org.junit.jupiter.api.Assertions`。测试类位于 `src/test/java`，类名以 `Test` 结尾，方法/类可保持包私有可见性，无需 `public`。
- **工具复用**：实现前先查 `mica-core/.../utils/` 是否已有同名或等价能力。
- **注释与 Javadoc**：公开类/方法需有 Javadoc；现有 Javadoc 为中文，补充时保持中文。

## 常用入口

- 版本号 / 发布信息：`mica-core/src/mica/java/net/dreamlu/mica/core/Mica.java`（由 `mica-auto` 在 `src/mica/java` 注入 `${version}`）。
- 启动回调示例：`mica-lite/src/main/java/net/dreamlu/mica/lite/launch/StartedEventListener.java`。
- 错误处理入口：`mica-lite/src/main/java/net/dreamlu/mica/lite/error/MicaErrorController.java`。
- 限流/缓存 starter：分别看 `mica-redis/.../ratelimiter/`、`mica-caffeine/.../config/`。

更多更新记录见 `CHANGELOG.md`，模块图见 `docs/img/mica2.x-open.jpg`。
