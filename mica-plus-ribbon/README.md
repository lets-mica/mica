# 负载均衡器

## 功能
1. 【优先级最高】ip 相同的服务（方便本地多服务联调）。
2. 可设置选择的 ip 或者 ip 段，例如：`172.21.0.*`、`172.21.0.8*`。
3. 可设定 tag，为了以后版本发布（灰度）做基础，可能还需要扩展。

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-plus-ribbon</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-plus-ribbon:${version}")
```

## 配置项

前缀：`mica.ribbon.rule`

| 配置项                     | 默认值 | 说明                                                     |
| ------------------------- | ------ | -------------------------------------------------------- |
| enabled          | true   | 是否启用                                                 |
| prior-ip-pattern | 空     | 优先的 ip 列表，支持通配符，例如：172.21.0.81、172.21.0.8*、172.21.0.*    |
| tag              | 无     | 服务的 tag，用于灰度，匹配：nacos.discovery.metadata.tag |

## 配置示例

例如：`dev`

```yaml
mica:
  ribbon:
    rule:
        prior-ip-pattern:
        - 172.21.0.*
```