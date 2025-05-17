package net.dreamlu;

import lombok.Data;
import net.dreamlu.mica.http.JsonPointer;

@Data
@JsonPointer("/Response")
public class JsonBean {
	@JsonPointer("/ResponseString")
	private String responseString;
	@JsonPointer("/Data/URL")
	private String url;
}
