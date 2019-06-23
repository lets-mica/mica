# 第三方登录组件

`mica-plus-social`，是 `mica-social` 的自动配置组件。

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-plus-social</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-plus-social:${version}")
```

### 配置
```yaml
mica:
  social:
    qq:
      client-id: xxxxxx
      client-secret: xxxxx
      redirect-uri: http://www.dreamlu.net/api/qq/callback
```

### 构造授权地址
```java
@Autowired
private AuthQqRequest authRequest;


@GetMapping("auth/qq")
public String auth() {
    return "redirect:" + authRequest.authorize();
}

@GetMapping("callback/qq")
public String callback(String code) {
	AuthResponse authResponse = authRequest.login(code);
    // 业务代码
    
}
```