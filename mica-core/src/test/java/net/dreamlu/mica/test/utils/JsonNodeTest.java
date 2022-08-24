package net.dreamlu.mica.test.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.utils.DateUtil;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * json node 测试
 *
 * @author L.cm
 */
class JsonNodeTest {

	@Test
	void test() {
		String json = "123123";
		JsonNode jsonNode = JsonUtil.readTree(json);

		Assertions.assertEquals(jsonNode.toString(), json);
	}

	@Test
	void test1() {
		String json = "{\"code\":1}";
		JsonNode jsonNode = JsonUtil.readTree(json);

		R<?> r = JsonUtil.getInstance().convertValue(jsonNode, R.class);

		Assertions.assertEquals(1, r.getCode());
	}

	@Test
	void test2() {
		String json1 = "1{\"code\":1}";
		boolean isJson1 = JsonUtil.isValidJson(json1);
		Assertions.assertFalse(isJson1);

		String json2 = "/**/{\"code\":1}";
		boolean isJson2 = JsonUtil.isValidJson(json2);
		Assertions.assertFalse(isJson2);

		String json3 = "{\"code\":1}";
		boolean isJson3 = JsonUtil.isValidJson(json3);
		Assertions.assertTrue(isJson3);
	}

	@Test
	void test3() {
		Date date = new Date();
		TestBean bean = new TestBean();
		bean.setDate(date);

		String dateTime = DateUtil.formatDateTime(date);
		TestBean testBean = JsonUtil.readValue(JsonUtil.toJson(bean), TestBean.class);
		Assertions.assertEquals(dateTime, DateUtil.formatDateTime(testBean.getDate()));
	}

	@Data
	static class TestBean {
		private Date date;
		private String xxx;
	}

}
