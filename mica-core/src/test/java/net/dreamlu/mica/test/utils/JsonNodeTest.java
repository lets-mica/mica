package net.dreamlu.mica.test.utils;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * json node 测试
 *
 * @author L.cm
 */
public class JsonNodeTest {

	@Test
	public void test() {
		String json = "123123";
		JsonNode jsonNode = JsonUtil.readTree(json);

		Assert.assertEquals(jsonNode.toString(), json);
	}

	@Test
	public void test1() {
		String json = "{\"code\":1}";
		JsonNode jsonNode = JsonUtil.readTree(json);

		R r = JsonUtil.getInstance().convertValue(jsonNode, R.class);

		Assert.assertEquals(r.getCode(), 1);
	}
}
