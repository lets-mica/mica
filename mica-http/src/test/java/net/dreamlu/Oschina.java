package net.dreamlu;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.http.CssQuery;

import java.util.List;

@Getter
@Setter
public class Oschina {

	@CssQuery(value = "head > title", attr = "text")
	private String title;

	@CssQuery(value = "#v_news .page a", inner = true)
	private List<VNews> vNews;

}
