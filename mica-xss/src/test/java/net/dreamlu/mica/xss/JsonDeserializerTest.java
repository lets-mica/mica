package net.dreamlu.mica.xss;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.xss.core.XssCleanDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
class JsonDeserializerTest {

	@Data
	static class DemoBean {
		private Integer pageNum;
		private Integer pageSize;
		@JsonDeserialize(using = XssCleanDeserializer.class)
		private String createDate;
		private Integer isRead;
	}

	@Data
	static class TypeBean {
		@JsonDeserialize(using = XssCleanDeserializer.class)
		private String pageNum;
		@JsonDeserialize(using = XssCleanDeserializer.class)
		private String pageSize;
	}

	@Test
	void test() {
		Assertions.assertThrows(MismatchedInputException.class, () -> {
			JsonMapper jsonMapper = new JsonMapper();
			DemoBean demoBean = jsonMapper.readValue("""
				{
				    "pageNum": 1,
				    "pageSize": 15,
				    "createDate":["12"],
				       "system": "1",
				       "isRead": 1,
				    "issue": "qweqweq"
				}""", DemoBean.class);
			log.info("demoBean:{}", demoBean);
		});
	}

	@Test
	void testType() {
		JsonMapper jsonMapper = new JsonMapper();
		TypeBean typeBean = jsonMapper.readValue("""
			{
			    "pageNum": 1,
			    "pageSize": 15
			}""", TypeBean.class);
		log.info("demoBean:{}", typeBean);
	}

}
