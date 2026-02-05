package net.dreamlu.mica.ddddocr.core;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ddddocr.exception.OCRException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 字符集管理器
 */
@Slf4j
class CharsetManager {
	private final List<String> charset;
	private final Set<Integer> validIndices;

	CharsetManager() {
		this.charset = new ArrayList<>();
		this.validIndices = new HashSet<>();
	}

	/**
	 * 加载默认字符集
	 */
	public void loadCharset(ResourceLoader resourceLoader) {
		Resource resource = resourceLoader.getResource("classpath:/models/charset.txt");
		charset.clear();
		// 添加blank字符（索引0），这是CTC解码需要的
		charset.add("");
		// 加载文件中的字符，过滤掉换行符和回车符，并去重
		Set<String> seen = new HashSet<>();
		try {
			String content = resource.getContentAsString(StandardCharsets.UTF_8);
			for (char c : content.toCharArray()) {
				if (c != '\n' && c != '\r') {
					String charStr = String.valueOf(c);
					if (!seen.contains(charStr)) {
						charset.add(charStr);
						seen.add(charStr);
					}
				}
			}
		} catch (IOException e) {
			throw new OCRException("加载字符集失败", e);
		}
		log.debug("成功加载字符集，共 {} 个字符（包含blank字符）", charset.size());
		updateValidIndices();
	}

	/**
	 * 更新有效索引（使用完整字符集）
	 */
	private void updateValidIndices() {
		validIndices.clear();
		for (int i = 0; i < charset.size(); i++) {
			validIndices.add(i);
		}
	}

	/**
	 * 获取字符集
	 */
	public List<String> getCharset() {
		return new ArrayList<>(charset);
	}

	/**
	 * 获取有效索引
	 */
	public Set<Integer> getValidIndices() {
		return new HashSet<>(validIndices);
	}
}
