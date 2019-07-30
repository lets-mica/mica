package net.dreamlu;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.http.CssQuery;

@Setter
@Getter
public class VNews {

	@CssQuery(value = "a", attr = "title")
	private String title;

	@CssQuery(value = "a", attr = "href")
	private String href;

}
