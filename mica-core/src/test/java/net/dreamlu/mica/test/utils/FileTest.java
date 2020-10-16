package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 文件测试
 *
 * @author L.cm
 */
public class FileTest {

	@Test
	public void test1() {
		String fileName = "123123123.xx.jpg";
		String fileExtension = FileUtil.getFileExtension(fileName);
		Assert.assertEquals("jpg", fileExtension);
		String extensionWithDot = FileUtil.getFileExtensionWithDot(fileName);
		Assert.assertEquals(".jpg", extensionWithDot);
		String pathWithoutExtension = FileUtil.getPathWithoutExtension(fileName);
		Assert.assertEquals("123123123.xx", pathWithoutExtension);
	}
}
