package net.dreamlu.mica;

import lombok.Data;

import java.util.Date;

@Data
public class User {
	private String id;
	private String name;
	private Integer age;
	private Date time;
//	private LocalDateTime now;
}
