package net.dreamlu.mica.mongo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.mongo.utils.JsonNodeInfo;
import net.dreamlu.mica.mongo.utils.MongoJsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * jsonnode 处理 工具
 *
 * @author L.cm
 */
public class MongoJsonUtilsTest {

	@Test
	public void test() {
		String json = "{\n" +
			"    \"id\": \"1\",\n" +
			"    \"accountId\": 12345678,\n" +
			"    \"testModel\": \"test\",\n" +
			"    \"type\": \"sys-setting\",\n" +
			"    \"settings\": {\n" +
			"        \"system_sound\": {\n" +
			"            \"sound_volume\": {\n" +
			"                \"sound_from_seat\": {\n" +
			"                    \"value\": \"10\",\n" +
			"                    \"version\": 123\n" +
			"                },\n" +
			"                \"sound_from_seat_test\": {\n" +
			"                    \"value\": \"10\",\n" +
			"                    \"version\": 123\n" +
			"                }\n" +
			"            },\n" +
			"            \"sound_volume_test\": {\n" +
			"                \"sound_from_seat\": {\n" +
			"                    \"value\": \"10\",\n" +
			"                    \"version\": 123\n" +
			"                }\n" +
			"            }\n" +
			"        },\n" +
			"        \"class1\": {\n" +
			"            \"class2\": {\n" +
			"                \"item\": {\n" +
			"                    \"value\": \"10\",\n" +
			"                    \"version\": 123\n" +
			"                }\n" +
			"            }\n" +
			"        }\n" +
			"    },\n" +
			"    \"createTime\": \"2018-12-19 18:00:00\"\n" +
			"}\n";

		JsonNode jsonNode = JsonUtil.readTree(json).at("/settings");

		List<JsonNodeInfo> jsonNodeInfoList = MongoJsonUtils.getLeafNodes(jsonNode);
		jsonNodeInfoList.forEach(x -> {
			System.out.println(x.getNodeKeys());
			System.out.println(x.getNodePath());
			System.out.println(x.getLeafNode());
		});

		Assert.assertTrue(!jsonNodeInfoList.isEmpty());

		List<String> xs = Arrays.asList("system_sound", "sound_volume", "sound_from_seat_test1");
		MongoJsonUtils.buildNode((ObjectNode) jsonNode, xs);
		System.out.println(jsonNode);
	}

}
