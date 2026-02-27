package net.dreamlu.mica.test.utils;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.utils.DateUtil;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JsonParser;
import tools.jackson.core.TreeNode;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.type.CollectionLikeType;
import tools.jackson.databind.type.MapType;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.*;

/**
 * JsonUtil 工具测试
 *
 * @author L.cm
 */
class JsonUtilTest {

	public static class Views {
		public static class Public { }
		public static class Internal extends Public { }
	}

	@Data
	public static class User {
		@JsonView(Views.Public.class)
		private Long id;

		@JsonView(Views.Public.class)
		private String name;

		@JsonView(Views.Internal.class)
		private String email;

		private LocalDateTime createTime;
	}

	@Data
	public static class TestBean {
		private String name;
		private Integer age;
		private List<String> hobbies;
		private Map<String, Object> metadata;
		private LocalDateTime createTime;
	}

	@Test
	void testToJson() {
		// 测试正常对象序列化
		User user = new User();
		user.setId(1L);
		user.setName("张三");
		user.setEmail("zhangsan@example.com");
		user.setCreateTime(LocalDateTime.now());

		String json = JsonUtil.toJson(user);
		Assertions.assertNotNull(json);
		Assertions.assertTrue(json.contains("\"name\":\"张三\""));
		Assertions.assertTrue(json.contains("\"id\":1"));

		// 测试 null 对象
		String nullJson = JsonUtil.toJson(null);
		Assertions.assertNull(nullJson);
	}

	@Test
	void testToJsonWithView() {
		User user = new User();
		user.setId(1L);
		user.setName("张三");
		user.setEmail("zhangsan@example.com");

		// 测试 Public 视图
		String publicJson = JsonUtil.toJsonWithView(user, Views.Public.class);
		Assertions.assertTrue(publicJson.contains("\"name\":\"张三\""));
		Assertions.assertTrue(publicJson.contains("\"id\":1"));
		Assertions.assertFalse(publicJson.contains("email"));

		// 测试 Internal 视图
		String internalJson = JsonUtil.toJsonWithView(user, Views.Internal.class);
		Assertions.assertTrue(internalJson.contains("\"email\":\"zhangsan@example.com\""));

		// 测试 null 对象
		String nullJson = JsonUtil.toJsonWithView(null, Views.Public.class);
		Assertions.assertNull(nullJson);
	}

	@Test
	void testToPrettyJson() {
		User user = new User();
		user.setId(1L);
		user.setName("张三");

		String prettyJson = JsonUtil.toPrettyJson(user);
		Assertions.assertNotNull(prettyJson);
		// 美化 JSON 应该包含换行和缩进
		Assertions.assertTrue(prettyJson.contains("\n"));

		// 测试 null 对象
		String nullPrettyJson = JsonUtil.toPrettyJson(null);
		Assertions.assertNull(nullPrettyJson);
	}

	@Test
	void testToJsonAsBytes() {
		User user = new User();
		user.setId(1L);
		user.setName("张三");

		byte[] jsonBytes = JsonUtil.toJsonAsBytes(user);
		Assertions.assertNotNull(jsonBytes);
		Assertions.assertTrue(jsonBytes.length > 0);

		// 测试 null 对象
		byte[] nullBytes = JsonUtil.toJsonAsBytes(null);
		Assertions.assertEquals(0, nullBytes.length);
	}

	@Test
	void testToJsonAsBytesWithView() {
		User user = new User();
		user.setId(1L);
		user.setName("张三");
		user.setEmail("test@example.com");

		byte[] publicBytes = JsonUtil.toJsonAsBytesWithView(user, Views.Public.class);
		Assertions.assertNotNull(publicBytes);
		Assertions.assertTrue(publicBytes.length > 0);
		String publicJson = new String(publicBytes);
		Assertions.assertFalse(publicJson.contains("email"));

		// 测试 null 对象
		byte[] nullBytes = JsonUtil.toJsonAsBytesWithView(null, Views.Public.class);
		Assertions.assertEquals(0, nullBytes.length);
	}

	@Test
	void testReadTree() {
		// 测试字符串
		String json = "{\"name\":\"张三\",\"age\":25}";
		JsonNode jsonNode = JsonUtil.readTree(json);
		Assertions.assertNotNull(jsonNode);
		Assertions.assertEquals("张三", jsonNode.get("name").asText());
		Assertions.assertEquals(25, jsonNode.get("age").asInt());

		// 测试字节数组
		byte[] jsonBytes = json.getBytes();
		JsonNode nodeFromBytes = JsonUtil.readTree(jsonBytes);
		Assertions.assertEquals("张三", nodeFromBytes.get("name").asText());

		// 测试 InputStream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
		JsonNode nodeFromStream = JsonUtil.readTree(inputStream);
		Assertions.assertEquals("张三", nodeFromStream.get("name").asText());

		// 测试 Reader
		StringReader reader = new StringReader(json);
		JsonNode nodeFromReader = JsonUtil.readTree(reader);
		Assertions.assertEquals("张三", nodeFromReader.get("name").asText());
	}

	@Test
	void testReadValue() {
		String json = "{\"name\":\"张三\",\"age\":25}";

		// 测试字符串转对象
		TestBean bean = JsonUtil.readValue(json, TestBean.class);
		Assertions.assertNotNull(bean);
		Assertions.assertEquals("张三", bean.getName());
		Assertions.assertEquals(25, bean.getAge().intValue());

		// 测试字节数组转对象
		byte[] jsonBytes = json.getBytes();
		TestBean beanFromBytes = JsonUtil.readValue(jsonBytes, TestBean.class);
		Assertions.assertEquals("张三", beanFromBytes.getName());

		// 测试 InputStream 转对象
		ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
		TestBean beanFromStream = JsonUtil.readValue(inputStream, TestBean.class);
		Assertions.assertEquals("张三", beanFromStream.getName());

		// 测试 Reader 转对象
		StringReader reader = new StringReader(json);
		TestBean beanFromReader = JsonUtil.readValue(reader, TestBean.class);
		Assertions.assertEquals("张三", beanFromReader.getName());

		// 测试 null 值
		TestBean nullBean = JsonUtil.readValue((String) null, TestBean.class);
		Assertions.assertNull(nullBean);

		TestBean nullBean2 = JsonUtil.readValue("", TestBean.class);
		Assertions.assertNull(nullBean2);
	}

	@Test
	void testReadValueWithParametrizedType() {
		String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";

		// 测试参数化类型 - 字符串输入
		List<TestBean> list = JsonUtil.readValue(json, List.class, TestBean.class);
		Assertions.assertNotNull(list);
		Assertions.assertEquals(2, list.size());
		Assertions.assertEquals("张三", list.get(0).getName());
		Assertions.assertEquals("李四", list.get(1).getName());

		// 测试参数化类型 - 字节数组输入 (这是您提到的新方法)
		byte[] jsonBytes = json.getBytes();
		List<TestBean> listFromBytes = JsonUtil.readValue(jsonBytes, List.class, TestBean.class);
		Assertions.assertNotNull(listFromBytes);
		Assertions.assertEquals(2, listFromBytes.size());
		Assertions.assertEquals("张三", listFromBytes.get(0).getName());
		Assertions.assertEquals("李四", listFromBytes.get(1).getName());

		// 测试 null 值 - 字符串
		List<TestBean> nullList = JsonUtil.readValue((String) null, List.class, TestBean.class);
		Assertions.assertNull(nullList);

		// 测试 null 值 - 字节数组
		List<TestBean> nullListFromBytes = JsonUtil.readValue((byte[]) null, List.class, TestBean.class);
		Assertions.assertNull(nullListFromBytes);

		// 测试空字节数组
		List<TestBean> emptyListFromBytes = JsonUtil.readValue(new byte[0], List.class, TestBean.class);
		Assertions.assertNull(emptyListFromBytes);
	}

	@Test
	void testReadValueWithTypeReference() {
		String json = "{\"users\":[{\"name\":\"张三\",\"age\":25}]}";

		TypeReference<Map<String, List<TestBean>>> typeRef = new TypeReference<Map<String, List<TestBean>>>() {};

		// 测试 TypeReference
		Map<String, List<TestBean>> map = JsonUtil.readValue(json, typeRef);
		Assertions.assertNotNull(map);
		Assertions.assertTrue(map.containsKey("users"));
		Assertions.assertEquals(1, map.get("users").size());
		Assertions.assertEquals("张三", map.get("users").get(0).getName());

		// 测试 null 值
		Map<String, List<TestBean>> nullMap = JsonUtil.readValue((String) null, typeRef);
		Assertions.assertNull(nullMap);
	}

	@Test
	void testReadValueWithJavaType() {
		String json = "[{\"name\":\"张三\",\"age\":25}]";

		JavaType javaType = JsonUtil.getListType(TestBean.class);

		// 测试 JavaType
		List<TestBean> list = JsonUtil.readValue(json, javaType);
		Assertions.assertNotNull(list);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals("张三", list.get(0).getName());

		// 测试 JavaType - 字节数组输入
		byte[] jsonBytes = json.getBytes();
		List<TestBean> listFromBytes = JsonUtil.readValue(jsonBytes, javaType);
		Assertions.assertNotNull(listFromBytes);
		Assertions.assertEquals(1, listFromBytes.size());
		Assertions.assertEquals("张三", listFromBytes.get(0).getName());

		// 测试 null 值
		List<TestBean> nullList = JsonUtil.readValue((String) null, javaType);
		Assertions.assertNull(nullList);

		// 测试 null 值 - 字节数组
		List<TestBean> nullListFromBytes = JsonUtil.readValue((byte[]) null, javaType);
		Assertions.assertNull(nullListFromBytes);
	}

	@Test
	void testReadValueWithBytesAndParametrizedType() {
		// 专门测试 readValue(byte[], Class<?>, Class<?>...) 方法
		String jsonArray = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";
		byte[] jsonBytes = jsonArray.getBytes();

		// 测试简单参数化类型 - List<TestBean>
		List<TestBean> listResult = JsonUtil.readValue(jsonBytes, List.class, TestBean.class);
		Assertions.assertNotNull(listResult);
		Assertions.assertEquals(2, listResult.size());
		Assertions.assertEquals("张三", listResult.get(0).getName());
		Assertions.assertEquals("李四", listResult.get(1).getName());

		// 测试复杂参数化类型 - Map<String, String>
		String mapJson = "{\"name\":\"张三\",\"city\":\"北京\"}";
		byte[] mapJsonBytes = mapJson.getBytes();
		Map<String, String> mapResult = JsonUtil.readValue(mapJsonBytes, Map.class, String.class, String.class);
		Assertions.assertNotNull(mapResult);
		Assertions.assertEquals("张三", mapResult.get("name"));
		Assertions.assertEquals("北京", mapResult.get("city"));

		// 测试边界情况
		List<TestBean> nullResult = JsonUtil.readValue((byte[]) null, List.class, TestBean.class);
		Assertions.assertNull(nullResult);

		List<TestBean> emptyResult = JsonUtil.readValue(new byte[0], List.class, TestBean.class);
		Assertions.assertNull(emptyResult);
	}

	@Test
	void testReadList() {
		String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";

		// 测试字符串读取列表
		List<TestBean> list = JsonUtil.readList(json, TestBean.class);
		Assertions.assertNotNull(list);
		Assertions.assertEquals(2, list.size());
		Assertions.assertEquals("张三", list.get(0).getName());

		// 测试字节数组读取列表
		byte[] jsonBytes = json.getBytes();
		List<TestBean> listFromBytes = JsonUtil.readList(jsonBytes, TestBean.class);
		Assertions.assertEquals(2, listFromBytes.size());

		// 测试 InputStream 读取列表
		ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
		List<TestBean> listFromStream = JsonUtil.readList(inputStream, TestBean.class);
		Assertions.assertEquals(2, listFromStream.size());

		// 测试 Reader 读取列表
		StringReader reader = new StringReader(json);
		List<TestBean> listFromReader = JsonUtil.readList(reader, TestBean.class);
		Assertions.assertEquals(2, listFromReader.size());

		// 测试 null 值
		List<TestBean> nullList = JsonUtil.readList((String) null, TestBean.class);
		Assertions.assertTrue(nullList.isEmpty());
	}

	@Test
	void testReadMap() {
		String json = "{\"user1\":{\"name\":\"张三\",\"age\":25},\"user2\":{\"name\":\"李四\",\"age\":30}}";

		// 测试读取 Map<String, Object>
		Map<String, Object> map = JsonUtil.readMap(json);
		Assertions.assertNotNull(map);
		Assertions.assertTrue(map.containsKey("user1"));

		// 测试读取 Map<String, TestBean>
		Map<String, TestBean> typedMap = JsonUtil.readMap(json, TestBean.class);
		Assertions.assertNotNull(typedMap);
		Assertions.assertEquals("张三", typedMap.get("user1").getName());

		// 测试读取 Map<String, TestBean> 字节数组
		byte[] jsonBytes = json.getBytes();
		Map<String, TestBean> mapFromBytes = JsonUtil.readMap(jsonBytes, TestBean.class);
		Assertions.assertEquals("张三", mapFromBytes.get("user1").getName());

		// 测试读取 Map<String, TestBean> InputStream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
		Map<String, TestBean> mapFromStream = JsonUtil.readMap(inputStream, TestBean.class);
		Assertions.assertEquals("张三", mapFromStream.get("user1").getName());

		// 测试读取 Map<String, TestBean> Reader
		StringReader reader = new StringReader(json);
		Map<String, TestBean> mapFromReader = JsonUtil.readMap(reader, TestBean.class);
		Assertions.assertEquals("张三", mapFromReader.get("user1").getName());

		// 测试自定义键类型
		String json2 = "{\"1\":{\"name\":\"张三\"},\"2\":{\"name\":\"李四\"}}";
		Map<Integer, TestBean> intKeyMap = JsonUtil.readMap(json2, Integer.class, TestBean.class);
		Assertions.assertEquals("张三", intKeyMap.get(1).getName());

		// 测试 null 值
		Map<String, TestBean> nullMap = JsonUtil.readMap((String) null, TestBean.class);
		Assertions.assertTrue(nullMap.isEmpty());
	}

	@Test
	void testConvertValue() {
		TestBean bean = new TestBean();
		bean.setName("张三");
		bean.setAge(25);

		// 测试对象转换
		Map<String, Object> map = JsonUtil.convertValue(bean, Map.class);
		Assertions.assertNotNull(map);
		Assertions.assertEquals("张三", map.get("name"));

		// 测试 JavaType 转换
		JavaType mapType = JsonUtil.getMapType(String.class, Object.class);
		Map<String, Object> map2 = JsonUtil.convertValue(bean, mapType);
		Assertions.assertEquals("张三", map2.get("name"));

		// 测试 TypeReference 转换
		TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
		Map<String, Object> map3 = JsonUtil.convertValue(bean, typeRef);
		Assertions.assertEquals("张三", map3.get("name"));
	}

	@Test
	void testTreeToValue() {
		String json = "{\"name\":\"张三\",\"age\":25}";
		JsonNode jsonNode = JsonUtil.readTree(json);

		// 测试 TreeNode 转对象
		TestBean bean = JsonUtil.treeToValue(jsonNode, TestBean.class);
		Assertions.assertNotNull(bean);
		Assertions.assertEquals("张三", bean.getName());
		Assertions.assertEquals(25, bean.getAge().intValue());

		// 测试 TreeNode 转 JavaType
		JavaType beanType = JsonUtil.getType(TestBean.class);
		TestBean bean2 = JsonUtil.treeToValue(jsonNode, beanType);
		Assertions.assertEquals("张三", bean2.getName());
	}

	@Test
	void testValueToTree() {
		TestBean bean = new TestBean();
		bean.setName("张三");
		bean.setAge(25);

		// 测试对象转 TreeNode
		JsonNode jsonNode = JsonUtil.valueToTree(bean);
		Assertions.assertNotNull(jsonNode);
		Assertions.assertEquals("张三", jsonNode.get("name").asText());
		Assertions.assertEquals(25, jsonNode.get("age").asInt());
	}

	@Test
	void testIsValidJson() {
		// 测试有效的 JSON 对象
		String validObject = "{\"name\":\"张三\",\"age\":25}";
		Assertions.assertTrue(JsonUtil.isValidJson(validObject));

		// 测试有效的 JSON 数组
		String validArray = "[{\"name\":\"张三\"},{\"name\":\"李四\"}]";
		Assertions.assertTrue(JsonUtil.isValidJson(validArray));

		// 测试无效的 JSON
		String invalidJson = "invalid json";
		Assertions.assertFalse(JsonUtil.isValidJson(invalidJson));

		// 测试带注释的 JSON（应该失败）
		String commentedJson = "// comment\n{\"name\":\"张三\"}";
		Assertions.assertFalse(JsonUtil.isValidJson(commentedJson));

		// 测试末尾有额外内容的 JSON
		String trailingJson = "{\"name\":\"张三\"} extra content";
		Assertions.assertFalse(JsonUtil.isValidJson(trailingJson));

		// 测试字节数组
		byte[] validBytes = validObject.getBytes();
		Assertions.assertTrue(JsonUtil.isValidJson(validBytes));

		// 测试 InputStream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(validBytes);
		Assertions.assertTrue(JsonUtil.isValidJson(inputStream));

		// 测试 Reader
		StringReader reader = new StringReader(validObject);
		Assertions.assertTrue(JsonUtil.isValidJson(reader));
	}

	@Test
	void testCreateNodes() {
		// 测试创建 ObjectNode
		ObjectNode objectNode = JsonUtil.createObjectNode();
		Assertions.assertNotNull(objectNode);
		objectNode.put("name", "张三");
		objectNode.put("age", 25);
		Assertions.assertEquals("张三", objectNode.get("name").asText());

		// 测试创建 ArrayNode
		ArrayNode arrayNode = JsonUtil.createArrayNode();
		Assertions.assertNotNull(arrayNode);
		arrayNode.add("张三");
		arrayNode.add("李四");
		Assertions.assertEquals(2, arrayNode.size());
		Assertions.assertEquals("张三", arrayNode.get(0).asText());
	}

	@Test
	void testTypeMethods() {
		// 测试 getType
		JavaType userType = JsonUtil.getType(User.class);
		Assertions.assertNotNull(userType);
		Assertions.assertEquals(User.class, userType.getRawClass());

		// 测试 getMapType
		MapType mapType = JsonUtil.getMapType(String.class, TestBean.class);
		Assertions.assertNotNull(mapType);
		Assertions.assertEquals(Map.class, mapType.getRawClass());
		Assertions.assertEquals(String.class, mapType.getKeyType().getRawClass());
		Assertions.assertEquals(TestBean.class, mapType.getContentType().getRawClass());

		// 测试 getListType
		CollectionLikeType listType = JsonUtil.getListType(TestBean.class);
		Assertions.assertNotNull(listType);
		Assertions.assertEquals(List.class, listType.getRawClass());
		Assertions.assertEquals(TestBean.class, listType.getContentType().getRawClass());

		// 测试 getParametricType
		JavaType parametricType = JsonUtil.getParametricType(List.class, TestBean.class);
		Assertions.assertNotNull(parametricType);
		Assertions.assertEquals(List.class, parametricType.getRawClass());
	}

	@Test
	void testDateHandling() {
		TestBean bean = new TestBean();
		bean.setName("张三");
		bean.setAge(25);
		bean.setCreateTime(LocalDateTime.now());

		String json = JsonUtil.toJson(bean);
		Assertions.assertNotNull(json);
		Assertions.assertTrue(json.contains("createTime"));

		TestBean deserializedBean = JsonUtil.readValue(json, TestBean.class);
		Assertions.assertNotNull(deserializedBean);
		Assertions.assertEquals("张三", deserializedBean.getName());
	}

	@Test
	void testComplexObject() {
		TestBean bean = new TestBean();
		bean.setName("张三");
		bean.setAge(25);

		List<String> hobbies = Arrays.asList("读书", "游泳", "编程");
		bean.setHobbies(hobbies);

		Map<String, Object> metadata = new HashMap<>();
		metadata.put("city", "北京");
		metadata.put("active", true);
		bean.setMetadata(metadata);

		String json = JsonUtil.toJson(bean);
		Assertions.assertNotNull(json);

		TestBean deserializedBean = JsonUtil.readValue(json, TestBean.class);
		Assertions.assertNotNull(deserializedBean);
		Assertions.assertEquals("张三", deserializedBean.getName());
		Assertions.assertEquals(3, deserializedBean.getHobbies().size());
		Assertions.assertEquals("北京", deserializedBean.getMetadata().get("city"));
	}

	@Test
	void testExceptionHandling() {
		// 测试无效 JSON 解析异常
		Assertions.assertThrows(Exception.class, () -> {
			JsonUtil.readValue("invalid json", TestBean.class);
		});

		// 测试 null 输入的各种情况已在其他测试中覆盖
	}
}
