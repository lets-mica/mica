package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

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

	@Test
	public void test2() {
		String data = "123哈哈";
		String testFilePath = FileUtil.getTempDirPath() + File.separator + "1.txt";
		File testFile = new File(testFilePath);
		FileUtil.writeToFile(testFile, data, false);
		String read1 = FileUtil.readToString(testFile);
		Assert.assertEquals(data, read1);
		String read2 = FileUtil.readToString(testFile);
		Assert.assertEquals(data, read2);
		FileUtil.writeToFile(testFile, data, true);
		String read3 = FileUtil.readToString(testFile);
		Assert.assertEquals(data + data, read3);
		testFile.deleteOnExit();
	}

}
