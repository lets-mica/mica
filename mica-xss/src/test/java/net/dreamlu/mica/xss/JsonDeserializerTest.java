package net.dreamlu.mica.xss;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.xss.core.XssCleanDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
	void test() throws JsonProcessingException {
		Assertions.assertThrows(MismatchedInputException.class, () -> {
			ObjectMapper objectMapper = new ObjectMapper();
			DemoBean demoBean = objectMapper.readValue("""
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
	void testType() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeBean typeBean = objectMapper.readValue("""
			{
			    "pageNum": 1,
			    "pageSize": 15
			}""", TypeBean.class);
		log.info("demoBean:{}", typeBean);
	}

}
