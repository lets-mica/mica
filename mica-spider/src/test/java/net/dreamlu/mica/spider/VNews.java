package net.dreamlu.mica.spider;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.spider.mapper.CssQuery;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class VNews {

	@CssQuery(value = "a", attr = "title")
	private String title;

	@CssQuery(value = "a", attr = "href")
	private String href;

	@CssQuery(value = ".item-extra", attr = "text")
	@DateTimeFormat(pattern = "MM/dd")
	private Date date;

}
