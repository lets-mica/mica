## mica-bom
`mica-bom`，可以简化依赖和依赖版本统一处理，避免 jar 冲突。

### Maven
```xml
<dependencyManagement> 
  <dependencies> 
    <dependency> 
      <groupId>net.dreamlu</groupId>
      <artifactId>mica-bom</artifactId>
      <version>${mica.version}</version>
      <type>pom</type>
      <scope>import</scope> 
    </dependency> 
  </dependencies> 
</dependencyManagement>
```

### Gradle
Spring boot 环境中可以开启 `apply plugin: "io.spring.dependency-management"` 插件。

```groovy
dependencyManagement {
    imports {
        mavenBom "net.dreamlu:mica-bom:${micaVersion}"
    }
}
```

### 组件坐标
`groupId`：
```text
net.dreamlu
```

`artifactId`：
```text
mica-core
mica-http
mica-launcher
mica-log4j2
mica-boot
mica-boot-test
mica-captcha
mica-cloud
mica-plus-error-catch
mica-plus-redis
mica-plus-mongo
mica-plus-swagger
mica-plus-ribbon
```

## 使用 snapshots

`注意`：`snapshots` 版本会及时响应，修复最新的 `bug` 或者必要的需求。

### maven
```xml
<repositories>
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <releases>
            <enabled>false</enabled>
        </releases>
    </repository>
</repositories>
```

### gradle

```groovy
repositories {
    mavenLocal()
    maven { url "https://maven.aliyun.com/repository/public" }
    maven { url "https://maven.aliyun.com/repository/spring" }
    maven { url "https://maven.aliyun.com/repository/spring-plugin" }
    maven { url "https://repo.spring.io/libs-release" }
    maven { url "https://repo.spring.io/milestone" }
    // 添加 snapshots 库地址
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
    mavenCentral()
}
```