package net.dreamlu.mica.activerecord.test;

import com.jfinal.plugin.activerecord.Record;
import net.dreamlu.mica.activerecord.utils.ModelUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * ModelUtil test
 */
public class ModelUtilTest {

	@Test
	public void testRecord() {
		String value = "123";
		Record record = new Record();
		record.set("abc_test", value);
		User user = ModelUtil.toBean(record, User.class);
		Assert.assertEquals(value, user.getAbcTest());
	}

	@Test
	public void test() {
		String value = "123";
		UserModel userModel = new UserModel();
		userModel.setAbcTest(value);
		User user = ModelUtil.toBean(userModel, User.class);
		Assert.assertEquals(value, user.getAbcTest());
	}

}
