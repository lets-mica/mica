package net.dreamlu.mica.xss;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.xss.core.XssCleanDeserializer;
import org.junit.Test;

@Slf4j
public class JsonDeserializerTest {

	@Data
	static class DemoBean {
		private Integer pageNum;
		private Integer pageSize;
		@JsonDeserialize(using = XssCleanDeserializer.class)
		private String createDate;
		private Integer isRead;
	}

	@Test(expected = MismatchedInputException.class)
	public void test() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DemoBean demoBean = objectMapper.readValue("{\n"
			+ "    \"pageNum\": 1,\n"
			+ "    \"pageSize\": 15,\n"
			+ " \n"
			+ "    \"createDate\":[\"12\"],\n"
			+ "       \"system\": \"1\",\n"
			+ "       \"isRead\": 1,\n"
			+ "    \"issue\": \"qweqweq\"\n"
			+ "}", DemoBean.class);
		log.info("demoBean:{}", demoBean);
	}

}