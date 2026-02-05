package net.dreamlu.mica.ddddocr.core;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OnnxValue;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ddddocr.exception.OCRException;
import net.dreamlu.mica.ddddocr.utils.ImageIOUtil;
import org.springframework.core.io.ResourceLoader;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;

/**
 * OCR识别引擎
 */
@Slf4j
public class OCREngine implements Closeable {
	private final ModelLoader modelLoader;
	private final CharsetManager charsetManager;
	private OrtSession session;
	// resize格式: [width, height]，-1表示保持宽高比
	// 对于OCR验证码，通常固定高度，宽度自适应
	private final int[] resize = {-1, 64};
	private boolean isInitialized;

	public OCREngine(ResourceLoader resourceLoader) {
		this.modelLoader = new ModelLoader();
		this.charsetManager = new CharsetManager();
		this.isInitialized = false;
		initialize(resourceLoader);
	}

	private void initialize(ResourceLoader resourceLoader) {
		try {
			// 加载默认模型
			session = modelLoader.loadOcrModel();
			charsetManager.loadCharset(resourceLoader);
			isInitialized = true;
		} catch (Exception e) {
			throw new OCRException("OCR引擎初始化失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 执行OCR识别
	 */
	public String detectImage(Object image, boolean pngFix, boolean probability) {
		if (!isReady()) {
			throw new OCRException("OCR引擎未初始化");
		}
		try {
			// 加载图像
			BufferedImage pilImage = loadImage(image);
			// 预处理图像
			float[][][][] processedImage = preprocessImage(pilImage, pngFix);
			// 执行推理
			Object result = inference(processedImage, probability);
			if (result instanceof String) {
				return (String) result;
			}
			return result.toString();
		} catch (Exception e) {
			throw new OCRException("OCR识别失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 预处理图像
	 */
	private float[][][][] preprocessImage(BufferedImage image, boolean pngFix) {
		try {
			// 处理PNG透明背景
			if (pngFix && image.getType() == BufferedImage.TYPE_INT_ARGB) {
				image = ImageIOUtil.pngRgbaBlackPreprocess(image);
			}
			// 对于OCR验证码识别，应该保持宽高比，只固定高度
			// resize格式: [width, height]，-1表示保持宽高比
			int channel = 1;
			if (resize[0] == -1 && resize[1] != -1) {
				// 固定高度，宽度自适应（保持宽高比）
				return ImageIOUtil.prepareForOnnx(image, resize[1]);
			} else if (resize[0] != -1 && resize[1] != -1) {
				// 固定宽高（可能导致变形，不推荐用于OCR）
				image = ImageIOUtil.resizeImage(image, resize[0], resize[1]);
				BufferedImage gray = channel == 1 ? ImageIOUtil.convertToGrayscale(image) : image;
				int height = gray.getHeight();
				int width = gray.getWidth();
				float[][][][] result = new float[1][1][height][width];
				Raster raster = gray.getRaster();
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int grayValue;
						if (gray.getType() == BufferedImage.TYPE_BYTE_GRAY) {
							grayValue = raster.getSample(x, y, 0);
						} else {
							int rgb = gray.getRGB(x, y);
							grayValue = (rgb >> 16) & 0xFF;
						}
						result[0][0][y][x] = grayValue / 255.0f;
					}
				}
				return result;
			} else {
				// 不调整尺寸，直接处理
				BufferedImage gray = channel == 1 ? ImageIOUtil.convertToGrayscale(image) : image;
				int height = gray.getHeight();
				int width = gray.getWidth();
				float[][][][] result = new float[1][1][height][width];
				Raster raster = gray.getRaster();
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int grayValue;
						if (gray.getType() == BufferedImage.TYPE_BYTE_GRAY) {
							grayValue = raster.getSample(x, y, 0);
						} else {
							int rgb = gray.getRGB(x, y);
							grayValue = (rgb >> 16) & 0xFF;
						}
						result[0][0][y][x] = grayValue / 255.0f;
					}
				}
				return result;
			}
		} catch (Exception e) {
			throw new OCRException("图像预处理失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 执行模型推理
	 */
	private Object inference(float[][][][] imageArray, boolean probability) {
		try {
			// 转换为ONNX Tensor
			OnnxTensor inputTensor = OnnxTensor.createTensor(
				OrtEnvironment.getEnvironment(),
				imageArray
			);

			// 获取输入名称
			String inputName = session.getInputNames().iterator().next();
			Map<String, OnnxTensor> inputs = new HashMap<>();
			inputs.put(inputName, inputTensor);
			// 执行推理
			OrtSession.Result outputs = session.run(inputs);
			// 获取输出
			OnnxValue outputValue = outputs.get(0);
			Object outputObj = outputValue.getValue();

			// 清理输入tensor
			inputTensor.close();
			outputs.close();

			// 处理输出 - 需要根据实际输出格式转换
			float[][][] output;
			if (outputObj instanceof float[][][]) {
				output = (float[][][]) outputObj;
			} else if (outputObj instanceof float[][] output2D) {
				// 如果是 [sequence_length, num_classes] 格式，转换为 [1, sequence_length, num_classes]
				output = new float[1][output2D.length][];
				System.arraycopy(output2D, 0, output[0], 0, output2D.length);
			} else {
				throw new OCRException("不支持的输出格式: " + outputObj.getClass().getName());
			}
			// 处理输出
			if (probability) {
				return processProbabilityOutput(output);
			} else {
				return processTextOutput(output);
			}
		} catch (Exception e) {
			throw new OCRException("模型推理失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 处理文本输出
	 */
	private String processTextOutput(float[][][] output) {
		try {
			// 先应用softmax，然后获取预测结果
			float[][][] probabilities = softmax(output);
			int[] predictedIndices;
			if (probabilities.length == 1) {
				// 形状为 [1, sequence_length, num_classes]
				int seqLen = probabilities[0].length;
				predictedIndices = new int[seqLen];
				for (int i = 0; i < seqLen; i++) {
					predictedIndices[i] = argmax(probabilities[0][i]);
				}
			} else {
				// 其他形状，如 [sequence_length, 1, num_classes]
				int seqLen = probabilities.length;
				predictedIndices = new int[seqLen];
				for (int i = 0; i < seqLen; i++) {
					if (probabilities[i].length == 1) {
						// [sequence_length, 1, num_classes]
						predictedIndices[i] = argmax(probabilities[i][0]);
					} else {
						// 如果 probabilities[i] 是二维数组，需要找到正确的维度
						// 通常应该是 [sequence_length, 1, num_classes] 格式
						// 取第一个子数组
						if (probabilities[i].length > 0) {
							predictedIndices[i] = argmax(probabilities[i][0]);
						} else {
							predictedIndices[i] = 0;
						}
					}
				}
			}
			// CTC解码
			List<Integer> decodedIndices = ctcDecode(predictedIndices);
			// 转换为字符
			List<String> charset = charsetManager.getCharset();
			Set<Integer> validIndices = charsetManager.getValidIndices();
			// 检查字符集大小是否与模型输出维度匹配
			int expectedCharsetSize = output[0][0].length; // num_classes
			if (charset.size() != expectedCharsetSize) {
				log.warn("字符集大小 ({}) 与模型输出维度 ({}) 不匹配", charset.size(), expectedCharsetSize);
			}
			StringBuilder result = new StringBuilder();
			for (int idx : decodedIndices) {
				// 只处理有效的索引范围
				if (idx >= 0 && idx < charset.size()) {
					// 检查是否在有效索引集合中（如果设置了范围限制）
					if (validIndices.isEmpty() || validIndices.contains(idx)) {
						String ch = charset.get(idx);
						if (!ch.isEmpty()) {
							result.append(ch);
						}
					}
				}
			}
			log.debug("OCR识别结果: [{}]", result);
			return result.toString();
		} catch (Exception e) {
			throw new OCRException("文本输出处理失败: " + e.getMessage(), e);
		}
	}

	/**
	 * CTC解码：去除连续重复和blank字符
	 */
	private List<Integer> ctcDecode(int[] predictedIndices) {
		List<Integer> decoded = new ArrayList<>();
		Integer prevIdx = null;
		for (int idx : predictedIndices) {
			if (prevIdx == null || idx != prevIdx) {
				if (idx != 0) { // 跳过blank字符（索引0）
					decoded.add(idx);
				}
			}
			prevIdx = idx;
		}
		return decoded;
	}

	/**
	 * 处理概率输出
	 */
	private Map<String, Object> processProbabilityOutput(float[][][] output) {
		try {
			// 应用 softmax
			float[][][] probabilities = softmax(output);
			// 获取文本结果
			String textResult = processTextOutput(output);
			// 构建概率信息
			List<String> charset = charsetManager.getCharset();
			List<float[][]> probList = new ArrayList<>(Arrays.asList(probabilities));
			Map<String, Object> result = new HashMap<>();
			result.put("text", textResult);
			result.put("probabilities", probList);
			result.put("charset", charset);
			// 计算平均置信度
			double confidence = 0.0;
			int count = 0;
			for (float[][] prob : probabilities) {
				for (float[] p : prob) {
					float maxVal = Float.MIN_VALUE;
					for (float val : p) {
						if (val > maxVal) {
							maxVal = val;
						}
					}
					confidence += maxVal;
					count++;
				}
			}
			result.put("confidence", count > 0 ? confidence / count : 0.0);
			return result;
		} catch (Exception e) {
			throw new OCRException("概率输出处理失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 计算argmax
	 */
	private int argmax(float[] array) {
		int maxIdx = 0;
		float maxVal = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > maxVal) {
				maxVal = array[i];
				maxIdx = i;
			}
		}
		return maxIdx;
	}

	/**
	 * 计算 softmax
	 */
	private float[][][] softmax(float[][][] x) {
		float[][][] result = new float[x.length][][];
		for (int i = 0; i < x.length; i++) {
			result[i] = new float[x[i].length][];
			for (int j = 0; j < x[i].length; j++) {
				result[i][j] = softmax1D(x[i][j]);
			}
		}
		return result;
	}

	private float[] softmax1D(float[] x) {
		float[] exp = new float[x.length];
		float max = Float.MIN_VALUE;
		for (float val : x) {
			if (val > max) {
				max = val;
			}
		}
		float sum = 0.0f;
		for (int i = 0; i < x.length; i++) {
			exp[i] = (float) Math.exp(x[i] - max);
			sum += exp[i];
		}
		for (int i = 0; i < x.length; i++) {
			exp[i] /= sum;
		}
		return exp;
	}

	/**
	 * 检查引擎是否就绪
	 */
	private boolean isReady() {
		return isInitialized && session != null;
	}

	@Override
	public void close() throws IOException {
		// 清理资源
		if (session != null) {
			try {
				session.close();
			} catch (Exception e) {
				// 忽略关闭错误
			}
			session = null;
		}
		isInitialized = false;
	}

	/**
	 * OCR识别
	 */
	public String detectImage(byte[] imageBytes) {
		return detectImage(ImageIOUtil.loadImage(imageBytes));
	}

	/**
	 * OCR识别
	 */
	public String detectImage(String imagePath) {
		return detectImage(ImageIOUtil.loadImage(imagePath), false, false);
	}

	/**
	 * OCR识别
	 */
	public String detectImage(BufferedImage image) {
		return detectImage(image, false, false);
	}

	/**
	 * 识别 base64 图片中的文本
	 */
	public String detectBase64Image(String base64Image) {
		return detectImage(ImageIOUtil.loadBase64Image(base64Image));
	}

	/**
	 * 加载图像
	 */
	private BufferedImage loadImage(Object image) {
		if (image instanceof byte[]) {
			return ImageIOUtil.loadImage((byte[]) image);
		} else if (image instanceof String) {
			return ImageIOUtil.loadImage((String) image);
		} else if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		} else {
			throw new OCRException("不支持的图像类型: " + image.getClass().getName());
		}
	}

}
