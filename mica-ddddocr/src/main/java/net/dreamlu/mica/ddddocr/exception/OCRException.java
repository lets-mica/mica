package net.dreamlu.mica.ddddocr.exception;

/**
 * DDDDOCR 异常类
 *
 * @author L.cm
 */
public class OCRException extends RuntimeException {
	public OCRException(String message) {
		super(message);
	}

	public OCRException(String message, Throwable cause) {
		super(message, cause);
	}
}
