package net.dreamlu.mica.test.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import net.dreamlu.mica.core.beans.CopyProperty;

import java.util.List;

@Data
@Accessors(chain = true)
public class User1 {
	private String id;
	private Integer idInt;
	private String name;
	private String photo;
	@CopyProperty("gender")
	private String six;
	@CopyProperty(ignore = true)
	private Integer xx;
	private List<String> data;
}
