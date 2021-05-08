package net.dreamlu.mica.core.graalvm;

import org.springframework.util.ClassUtils;

/**
 * Native 工具
 *
 * <p>
 * 参考： NativeDetector 和 NativeListener
 * </p>
 *
 * @author L.cm
 */
public class NativeUtil {
	public static final String GENERATED_CLASS = "org.springframework.aot.StaticSpringFactories";

	public static final boolean GENERATED_CLASS_PRESENT = ClassUtils.isPresent(GENERATED_CLASS, null);

	/**
	 * Holds the string that is the name of the system property providing information about the
	 * context in which code is currently executing.
	 */
	public static final String PROPERTY_IMAGE_CODE_KEY = "org.graalvm.nativeimage.imagecode";

	/**
	 * See https://github.com/oracle/graal/blob/master/sdk/src/org.graalvm.nativeimage/src/org/graalvm/nativeimage/ImageInfo.java
	 */
	private static final boolean IS_IMAGE_CODE = (System.getProperty(PROPERTY_IMAGE_CODE_KEY) != null);

	/**
	 * Returns {@code true} if invoked in the context of image building or during image runtime, else {@code false}.
	 */
	public static boolean inNativeImage() {
		return IS_IMAGE_CODE || GENERATED_CLASS_PRESENT;
	}

}
