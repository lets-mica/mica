package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * 文件测试
 *
 * @author L.cm
 */
class FileTest {

	@Test
	void test1() {
		String fileName = "123123123.xx.jpg";
		String fileExtension = FileUtil.getFileExtension(fileName);
		Assertions.assertEquals("jpg", fileExtension);
		String extensionWithDot = FileUtil.getFileExtensionWithDot(fileName);
		Assertions.assertEquals(".jpg", extensionWithDot);
		String pathWithoutExtension = FileUtil.getPathWithoutExtension(fileName);
		Assertions.assertEquals("123123123.xx", pathWithoutExtension);
	}

	@Test
	void test2() {
		String data = "123哈哈";
		String testFilePath = FileUtil.getTempDirPath() + File.separator + "1.txt";
		File testFile = new File(testFilePath);
		FileUtil.writeToFile(testFile, data, false);
		String read1 = FileUtil.readToString(testFile);
		Assertions.assertEquals(data, read1);
		String read2 = FileUtil.readToString(testFile);
		Assertions.assertEquals(data, read2);
		FileUtil.writeToFile(testFile, data, true);
		String read3 = FileUtil.readToString(testFile);
		Assertions.assertEquals(data + data, read3);
		testFile.deleteOnExit();
	}

}
