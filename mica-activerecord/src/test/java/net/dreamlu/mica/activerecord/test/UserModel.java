package net.dreamlu.mica.activerecord.test;

import com.jfinal.plugin.activerecord.Model;

public class UserModel extends Model<UserModel> {

	public Long getId() {
		return getLong("id");
	}

	public UserModel setId(Long id) {
		super.set("id", id);
		return this;
	}

	public String getName() {
		return getStr("name");
	}

	public UserModel setName(String name) {
		super.set("name", name);
		return this;
	}

	public String getAbcTest() {
		return getStr("abc_test");
	}

	public UserModel setAbcTest(String abcTest) {
		super.set("abc_test", abcTest);
		return this;
	}
}
