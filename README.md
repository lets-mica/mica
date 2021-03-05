## mica（云母）
[![Java CI](https://github.com/lets-mica/mica/workflows/Java%20CI/badge.svg)](https://github.com/lets-mica/mica/actions)
![JAVA 8](https://img.shields.io/badge/JDK-1.8+-brightgreen.svg)
[![Mica Maven release](https://img.shields.io/nexus/r/https/oss.sonatype.org/net.dreamlu/mica-bom.svg?style=flat-square)](https://mvnrepository.com/artifact/net.dreamlu/mica-bom)
[![Mica maven snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/net.dreamlu/mica-bom.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/net/dreamlu/)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/eadcb54f2ec14f31b8abf3ab13ad2d4d)](https://app.codacy.com/app/ChunMengLu/mica?utm_source=github.com&utm_medium=referral&utm_content=lets-mica/mica&utm_campaign=Badge_Grade_Settings)
[![GitHub](https://img.shields.io/github/license/lets-mica/mica.svg?style=flat-square)](https://github.com/lets-mica/mica/blob/master/LICENSE)

`Mica`，Spring Cloud 微服务开发核心包，支持 `web` 和 `webflux`。

`注意`：`snapshots` 版本会及时响应，修复最新的 `bug` 或者必要的需求。

## 使用文档
![mica 2.x 模块图](docs/img/mica2.x-open.png)

**使用文档**详见：[mica2.x 使用文档](http://wiki.dreamlu.net)

**更新记录**详见：[Mica 更新记录](CHANGELOG.md)

## 版本说明
| 最新版本     | mica 版本         | spring boot 版本  | spring cloud 版本 |
| ----------- | ---------------- | ---------------- | ----------------- |
| 2.4.3-GA    | mica 2.4.x       | 2.4.x            | 2020              |
| 2.1.1-GA    | mica 2.0.x~2.1.x | 2.2.x ~ 2.3.x    | Hoxton            |

## mica 生态
- mica-auto (Spring boot starter 利器): https://gitee.com/596392912/mica-auto
- mica-weixin（jfinal weixin 的 spring boot starter）：https://gitee.com/596392912/mica-weixin
- mica-mqtt（基于 t-io 实现的 mqtt组件）：https://gitee.com/596392912/mica-mqtt
- Spring cloud 微服务 http2 方案（h2c）: https://gitee.com/596392912/spring-cloud-java11

## 版本号说明
`release` 版本号格式为 `x.x.x-GA`，响应 `冷神` 的吐槽，改短了一点。

`snapshots` 版本号格式为 `x.x.x-SNAPSHOT`，`snapshots` 版每次提交后会自动构建。

## 已知问题
lombok 生成的 method 问题：https://github.com/rzwitserloot/lombok/issues/1861

对于 xX 类属性名，第一个小写，第二个大写的 bean 属性名，Map -> Bean 或 Bean -> Map 存在问题。

不打算做兼容，待 lombok 新版修复。

## 开源协议
![LGPL v3](docs/img/lgplv3-147x51.png) 

**软著编号**：`2020SR0411603`

## 协议解释
LGPL（[GNU Lesser General Public License](http://www.gnu.org/licenses/lgpl.html)）

LGPL是GPL的一个为主要为类库使用设计的开源协议。和GPL要求任何使用/修改/衍生之GPL类库的的软件必须采用GPL协议不同。LGPL允许商业软件通过类库引用(link)方式使用LGPL类库而不需要开源商业软件的代码。这使得采用LGPL协议的开源代码可以被商业软件作为类库引用并发布和销售。

但是如果修改LGPL协议的代码或者衍生，则所有修改的代码，涉及修改部分的额外代码和衍生的代码都必须采用LGPL协议。因此LGPL协议的开源代码很适合作为第三方类库被商业软件引用，但不适合希望以LGPL协议代码为基础，通过修改和衍生的方式做二次开发的商业软件采用。

## 用户权益
允许以引入不改源码的形式免费用于学习、毕设、公司项目、私活等。

特殊情况修改代码，但仍然想闭源需经过作者同意。

对未经过授权和不遵循 lgpl 协议二次开源或者商业化我们将追究到底。

参考请注明：参考自 mica：https://github.com/lets-mica/mica ，另请遵循 lgpl 协议。

`注意`：若禁止条款被发现有权追讨 **19999** 的授权费。

## 授权用户（最佳实践）
* `pigx` 宇宙最强微服务（架构师必备）：https://pig4cloud.com
* `bladex` 完整的线上解决方案（企业生产必备）：https://bladex.vip

## 相关链接
* `示例项目`：[https://github.com/lets-mica/mica-example](https://github.com/lets-mica/mica-example)
* mica 源码 Github：[https://github.com/lets-mica](https://github.com/lets-mica)
* mica 源码 Gitee（码云）：[https://gitee.com/596392912/mica](https://gitee.com/596392912/mica)
* mica 性能压测：[https://github.com/lets-mica/mica-jmh](https://github.com/lets-mica/mica-jmh)
* 文档地址（官网）：[https://www.dreamlu.net/docs/](https://www.dreamlu.net/docs/)
* 文档地址（语雀-可关注订阅）：[https://www.yuque.com/dreamlu](https://www.yuque.com/dreamlu)

## 开源推荐
- `Avue` 一款基于 vue 可配置化的神奇框架：[https://gitee.com/smallweigit/avue](https://gitee.com/smallweigit/avue)
- `pig` 宇宙最强微服务（架构师必备）：[https://gitee.com/log4j/pig](https://gitee.com/log4j/pig)
- `SpringBlade` 完整的线上解决方案（企业开发必备）：[https://gitee.com/smallc/SpringBlade](https://gitee.com/smallc/SpringBlade)
- `IJPay` 支付 SDK 让支付触手可及：[https://gitee.com/javen205/IJPay](https://gitee.com/javen205/IJPay)
- `JustAuth` 史上最全的整合第三方登录的开源库: [https://github.com/zhangyd-c/JustAuth](https://github.com/zhangyd-c/JustAuth)
- `spring-boot-demo` 深度学习并实战 spring boot 的项目: [https://github.com/xkcoding/spring-boot-demo](https://github.com/xkcoding/spring-boot-demo)

## 鸣谢
感谢 JetBrains 提供的免费开源 License：

[![JetBrains](docs/img/jetbrains.png)](https://www.jetbrains.com/?from=mica)

感谢 `如梦技术VIP群` 小伙伴们的大力支持。

## 微信公众号

![如梦技术](docs/img/dreamlu-weixin.jpg)

精彩内容每日推荐！！！

![Stargazers over time](https://whnb.wang/img/596392912/mica)
