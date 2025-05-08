package net.dreamlu;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.http.JsonPointerUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonPointerTest {

	@Test
	void test() {
		String json = """
			{
				"Response":	{
					"ResponseURL":	"/LAPI/V1.0/Channels/106/Media/Video/Streams/RecordURL",
					"ResponseCode":	0,
					"CreatedID":	-1,
					"StatusCode":	0,
					"ResponseString":	"Succeed",
					"Data":	{
						"URL":	"rtsp://127.204.31.1:554/C106/DVideoAndAudio/S0/B1744992000/E1745078399/replay/"
					}
				}
			}
			""";
		JsonNode jsonNode = JsonUtil.readTree(json);
		JsonBean jsonBean = JsonPointerUtil.readValue(jsonNode, JsonBean.class);
		Assertions.assertNotNull(jsonBean);
		Assertions.assertEquals("Succeed", jsonBean.getResponseString());
	}

}
