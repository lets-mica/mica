package net.dreamlu.mica.spider;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.spider.mapper.CssQuery;

import java.util.List;

@Getter
@Setter
public class Oschina {

	@CssQuery(value = "head > title", attr = "text")
	private String title;

	@CssQuery(value = "#v_news .item", inner = true)
	private List<VNews> vNews;

	@CssQuery(value = ".blog-container .blog-list div", inner = true)
	private List<VBlog> vBlogList;

}
