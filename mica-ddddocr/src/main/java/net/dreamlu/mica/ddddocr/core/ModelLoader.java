package net.dreamlu.mica.ddddocr.core;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtLoggingLevel;
import ai.onnxruntime.OrtSession;
import net.dreamlu.mica.ddddocr.exception.OCRException;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ONNX模型加载器
 *
 * @author L.cm
 */
class ModelLoader implements Closeable {
	private final OrtEnvironment environment;

	ModelLoader() {
		try {
			// 设置日志级别为 ERROR，只显示错误信息，过滤警告日志
			this.environment = OrtEnvironment.getEnvironment(OrtLoggingLevel.ORT_LOGGING_LEVEL_ERROR);
		} catch (Exception e) {
			throw new OCRException("无法初始化ONNX Runtime环境", e);
		}
	}

	/**
	 * 加载OCR模型
	 */
	public OrtSession loadOcrModel() {
		String modelPath = getResourcePath("models/ddddocr.onnx");
		try {
			if (!Files.exists(Paths.get(modelPath))) {
				throw new OCRException("模型文件不存在: " + modelPath);
			}
			OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
			// 设置日志级别为 ERROR，只显示错误信息，过滤警告日志
			opts.setSessionLogLevel(OrtLoggingLevel.ORT_LOGGING_LEVEL_ERROR);
			return environment.createSession(modelPath, opts);
		} catch (OrtException e) {
			throw new OCRException("模型加载失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 获取资源文件路径
	 */
	private String getResourcePath(String resourceName) {
		try {
			// 尝试从resources目录加载
			URL url = getClass().getClassLoader().getResource(resourceName);
			if (url != null) {
				// 处理Windows路径问题：URL.getPath()可能返回/E:/...格式
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					try {
						// 使用toURI()然后转换为File，这样可以正确处理Windows路径
						return new File(url.toURI()).getAbsolutePath();
					} catch (java.net.URISyntaxException e) {
						// 如果URI转换失败，尝试直接处理路径
						String path = url.getPath();
						// 移除开头的斜杠（Windows路径如/E:/...）
						if (path.startsWith("/") && path.length() > 3 && path.charAt(2) == ':') {
							path = path.substring(1);
						}
						// 解码URL编码的字符
						path = java.net.URLDecoder.decode(path, StandardCharsets.UTF_8);
						return path;
					}
				} else if ("jar".equals(protocol)) {
					// 如果是jar包内的资源，需要提取到临时文件
					return extractResourceFromJar(url, resourceName);
				} else {
					// 其他协议，尝试直接使用路径
					String path = url.getPath();
					if (path.startsWith("/") && path.length() > 3 && path.charAt(2) == ':') {
						path = path.substring(1);
					}
					return java.net.URLDecoder.decode(path, StandardCharsets.UTF_8);
				}
			}
			// 尝试从当前目录加载
			File file = new File(resourceName);
			if (file.exists()) {
				return file.getAbsolutePath();
			}
			// 尝试从项目根目录的models目录加载
			file = new File(resourceName.substring(resourceName.indexOf('/') + 1));
			if (file.exists()) {
				return file.getAbsolutePath();
			}
			throw new OCRException("无法找到模型文件: " + resourceName);
		} catch (Exception e) {
			throw new OCRException("获取模型路径失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 从JAR包中提取资源文件到临时文件
	 */
	private String extractResourceFromJar(URL url, String resourceName) throws IOException {
		// 创建临时文件
		String fileName = StringUtils.getFilename(resourceName);
		String fileExtension = StringUtils.getFilenameExtension(fileName);
		File tempFile = File.createTempFile("ddddocr_" + fileName.replace(".", "_"), "." + fileExtension);
		tempFile.deleteOnExit();
		// 复制资源到临时文件
		try (
			InputStream is = url.openStream();
			FileOutputStream fos = new java.io.FileOutputStream(tempFile)
		) {
			StreamUtils.copy(is, fos);
		}
		return tempFile.getAbsolutePath();
	}

	@Override
	public void close() throws IOException {
		if (environment != null) {
			try {
				environment.close();
			} catch (Exception e) {
				// 忽略关闭错误
			}
		}
	}
}
