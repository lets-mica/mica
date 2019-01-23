package net.dreamlu.mica.test.utils;

import lombok.Data;
import net.dreamlu.mica.core.beans.CopyProperty;

@Data
public class User {
	private Integer id;
	private String name;
	private String photo;
	private String xx;
	@CopyProperty("six")
	private String gender;
}
