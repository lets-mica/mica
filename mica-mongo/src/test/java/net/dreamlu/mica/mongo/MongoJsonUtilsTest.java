package net.dreamlu.mica.mongo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.mongo.utils.JsonNodeInfo;
import net.dreamlu.mica.mongo.utils.MongoJsonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * jsonnode 处理 工具
 *
 * @author L.cm
 */
class MongoJsonUtilsTest {

	@Test
	void test() {
		String json = """
			{
			    "id": "1",
			    "accountId": 12345678,
			    "testModel": "test",
			    "type": "sys-setting",
			    "settings": {
			        "system_sound": {
			            "sound_volume": {
			                "sound_from_seat": {
			                    "value": "10",
			                    "version": 123
			                },
			                "sound_from_seat_test": {
			                    "value": "10",
			                    "version": 123
			                }
			            },
			            "sound_volume_test": {
			                "sound_from_seat": {
			                    "value": "10",
			                    "version": 123
			                }
			            }
			        },
			        "class1": {
			            "class2": {
			                "item": {
			                    "value": "10",
			                    "version": 123
			                }
			            }
			        }
			    },
			    "createTime": "2018-12-19 18:00:00"
			}
			""";

		JsonNode jsonNode = JsonUtil.readTree(json).at("/settings");

		List<JsonNodeInfo> jsonNodeInfoList = MongoJsonUtils.getLeafNodes(jsonNode);
		jsonNodeInfoList.forEach(x -> {
			System.out.println(x.getNodeKeys());
			System.out.println(x.getNodePath());
			System.out.println(x.getLeafNode());
		});

		Assertions.assertTrue(!jsonNodeInfoList.isEmpty());

		List<String> xs = Arrays.asList("system_sound", "sound_volume", "sound_from_seat_test1");
		MongoJsonUtils.buildNode((ObjectNode) jsonNode, xs);
		System.out.println(jsonNode);
	}

}
