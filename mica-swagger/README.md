# mica-swagger 组件

## 说明
对 `swagger` 和 `knife4j` 的封装，方便使用。

**注意**: 添加 `@Api` 注解的控制器会自动暴露给 `swagger`。

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-swagger</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-swagger:${version}")
```

## 配置

| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| mica.swagger.enabled | true | 是否开启 swagger，默认：true |
| mica.swagger.title | XXX服务 | 标题，默认：XXX服务 |
| mica.swagger.description | XXX服务 | 详情，默认：XXX服务 |
| mica.swagger.version | V1.0 | 版本号，默认：V1.0 |
| mica.swagger.headers | 无 | 全局统一请求头 |
| mica.swagger.contact-email | 无 | 组织邮箱 |
| mica.swagger.contact-url | 无 | 组织url |
| mica.swagger.contact-user | 无 | 组织名 |
| mica.swagger.authorization.auth-regex | ^.*$ | 需要开启鉴权URL的正则，默认：^.*$ |
| mica.swagger.authorization.enabled | false | 开启Authorization，默认：false |
| mica.swagger.authorization.key-name | TOKEN | 鉴权传递的Header参数，默认：TOKEN |
| mica.swagger.authorization.name | Authorization | 鉴权策略ID，对应 SecurityReferences ID，默认：Authorization |

## 使用
**注意**: 添加有 `@Api` 注解的控制器才会自动暴露给 `swagger`。

```java
/**
 * demo 服务
 *
 * @author L.cm
 */
@Validated
@RestController
@Api("demo服务")
@RequiredArgsConstructor
public class DemoController {
	private final AccountService accountService;

	@ApiOperation("demo")
	@GetMapping("/demo")
	public R<AccountVO> demo() {
		return accountService.getAccount();
	}

}
```
