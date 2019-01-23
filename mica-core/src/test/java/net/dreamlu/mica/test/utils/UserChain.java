package net.dreamlu.mica.test.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import net.dreamlu.mica.core.beans.CopyProperty;

@Data
@Accessors(chain = true)
public class UserChain {
	private Integer id;
	private String name;
	private String photo;
	@CopyProperty(ignore = true)
	private String xx;
	private int[] a;
	private Integer allowNull;
}
