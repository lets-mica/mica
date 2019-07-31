package net.dreamlu;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.core.utils.DateUtil;
import net.dreamlu.mica.http.CssQuery;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class VNews {

	@CssQuery(value = "a", attr = "title")
	private String title;

	@CssQuery(value = "a", attr = "href")
	private String href;

	@CssQuery(value = ".news-date", attr = "text")
	@DateTimeFormat(pattern = "MM/dd")
	private Date date;

}
