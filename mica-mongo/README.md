# 说明文档
用于 `jackson` 和 `mongo` 复杂 tree 处理。

## 功能
* 将模型属性定义为 JsonNode 对象，自动序列号和反序列化。

## 示例
```java
@Data
@Persistent
@Document(collection = "testConfig")
public class TestConfig {

	@Id
	private String id;

	private JsonNode settings;

	// ... 

}
```
