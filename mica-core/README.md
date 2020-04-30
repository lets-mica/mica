# mica 核心包

## 功能
1. Cglib Bean copy 增强，支持链式 bean、Map、优化性能和支持类型转换。
2. 表单枚举接收增强，同 jackson 保持一致。
3. 服务异常。
4. 统一返回模型。
5. 常用工具包。

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-core</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-core:${version}")
```