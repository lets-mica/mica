package net.dreamlu.mica;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MediaTypeTest {

	public static void main(String[] args) {
		MediaType json = new MediaType("application", "vnd.mica.v1+json");
		System.out.println(json.toString());
		Map<String, String> xxx = new HashMap<>();
		xxx.put("a", "1231");
		MediaType jsonx = new MediaType(MediaType.APPLICATION_JSON, xxx);
		System.out.println(jsonx.toString());
	}
}
