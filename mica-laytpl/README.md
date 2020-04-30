# laytpl 模板（将其引入到 java 中）

具体可见 [《震惊，java8 Nashorn和laytpl居然能擦出这样火花！》](https://my.oschina.net/qq596392912/blog/872813)

## 注意
jmh 实测性能不是很出色，约为 `Thymeleaf` 的 `1/2` 适合用于对性能不是特别高的场景。例如：代码生成等。

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-laytpl</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-laytpl:${version}")
```

