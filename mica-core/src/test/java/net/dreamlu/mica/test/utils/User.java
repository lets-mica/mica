package net.dreamlu.mica.test.utils;

import lombok.Data;
import net.dreamlu.mica.core.beans.CopyProperty;
import net.dreamlu.mica.core.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class User {
	private Integer id;
	private int id1;
	private int[] ids;
	private int[] idss;
	private int[] idx;
	private String name;
	private String photo;
	private String xx;
	@CopyProperty("six")
	private String gender;
	private int xInt;
	private int xxInt;
	private long xLong;
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	private LocalDateTime birthday;
}
