package net.dreamlu.mica.test.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * 文件 测试
 *
 * doc: https://docs.oracle.com/javase/tutorial/essential/io/notification.html
 *
 * @author L.cm
 */
public class FileWatchTest {

	public static void main(String[] args) throws IOException {
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dir = new File("G:\\data").toPath();
		WatchKey key = dir.register(watcher, ENTRY_CREATE);
		while (true) {
			for (WatchEvent<?> event : key.pollEvents()) {
				System.out.println("12312");
			}
		}
	}
}
