package net.dreamlu.mica.lite.error;

import net.dreamlu.mica.core.context.DefaultMicaContext;
import net.dreamlu.mica.core.context.IMicaContext;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 文件上传相关异常的处理测试
 *
 * @author L.cm
 */
class RestExceptionTranslatorTest {

	/**
	 * 正常上传文件
	 */
	@Test
	void uploadFileSuccess() throws Exception {
		MvcResult result = mockMvc()
			.perform(multipart("/upload/param").file(new MockMultipartFile("file", "mica.txt",
				"text/plain", "mica".getBytes(StandardCharsets.UTF_8))))
			.andExpect(status().isOk())
			.andReturn();
		assertTrue(body(result).contains("\"code\":" + SystemCode.SUCCESS.getCode()));
	}

	/**
	 * 请求不是 multipart 请求，不应该被当成未知异常（500）处理
	 */
	@Test
	void notMultipartRequest() throws Exception {
		MvcResult result = mockMvc()
			.perform(post("/upload/param")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content("name=mica"))
			.andExpect(status().isBadRequest())
			.andReturn();
		String body = body(result);
		assertTrue(body.contains("\"code\":" + SystemCode.MULTIPART_ERROR.getCode()), body);
		assertTrue(body.contains("Current request is not a multipart request"), body);
	}

	/**
	 * 上传文件超过大小限制
	 */
	@Test
	void fileTooLarge() throws Exception {
		MvcResult result = mockMvc()
			.perform(multipart("/upload/too-large"))
			.andExpect(status().isPayloadTooLarge())
			.andReturn();
		String body = body(result);
		assertTrue(body.contains("\"code\":" + SystemCode.FILE_TOO_LARGE.getCode()), body);
	}

	/**
	 * 缺少必要的上传文件
	 */
	@Test
	void missingRequestPart() throws Exception {
		MvcResult result = mockMvc()
			.perform(multipart("/upload/part").file(new MockMultipartFile("other", "mica.txt",
				"text/plain", "mica".getBytes(StandardCharsets.UTF_8))))
			.andExpect(status().isBadRequest())
			.andReturn();
		String body = body(result);
		assertTrue(body.contains("\"code\":" + SystemCode.PARAM_MISS.getCode()), body);
		assertTrue(body.contains("file"), body);
	}

	/**
	 * 直接调用处理器，校验返回的业务码
	 */
	@Test
	void handlerResultCode() {
		RestExceptionTranslator translator = new RestExceptionTranslator();
		R<Object> result = translator.handleError(new MaxUploadSizeExceededException(1024L));
		assertEquals(SystemCode.FILE_TOO_LARGE.getCode(), result.getCode());
		assertEquals("上传文件超过大小限制", result.getMsg());
	}

	private static MockMvc mockMvc() {
		return MockMvcBuilders.standaloneSetup(new UploadController())
			.setControllerAdvice(new RestExceptionTranslator(), micaExceptionTranslator())
			.build();
	}

	private static MicaExceptionTranslator micaExceptionTranslator() {
		ObjectProvider<IMicaContext> micaContextProvider = new ObjectProvider<>() {
			@Override
			public IMicaContext getObject() {
				return new DefaultMicaContext();
			}

			@Override
			public IMicaContext getIfAvailable() {
				return new DefaultMicaContext();
			}
		};
		ApplicationEventPublisher publisher = event -> {
		};
		return new MicaExceptionTranslator(micaContextProvider, publisher);
	}

	private static String body(MvcResult result) throws Exception {
		return result.getResponse().getContentAsString(StandardCharsets.UTF_8);
	}

	@RestController
	static class UploadController {

		@PostMapping("/upload/param")
		public R<Object> uploadParam(@RequestParam("file") MultipartFile file) {
			return R.ok(file.getOriginalFilename());
		}

		@PostMapping("/upload/part")
		public R<Object> uploadPart(@RequestPart("file") MultipartFile file) {
			return R.ok(file.getOriginalFilename());
		}

		@PostMapping("/upload/too-large")
		public R<Object> tooLarge() {
			throw new MaxUploadSizeExceededException(1024L);
		}

	}

}
