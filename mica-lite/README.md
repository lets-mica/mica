# mica-lite 组件

基于 VIP 版 mica-boot 抽取部分功能，使得 Spring boot 单服务可以直接使用。

## 功能
- 全局异常处理。
- 文件上传目录配置。
- 启动信息打印。

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-lite</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-lite:${version}")
```

