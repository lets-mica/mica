package net.dreamlu.mica.reactive.logger;

/**
 * webflux 日志请求排除规则。
 *
 * @author L.cm
 */
public interface RequestLogExclusiveRule {

	/**
	 * 需要排除的部分请求
	 *
	 * <p>
	 * 规范：
	 * 1. 排除 /actuator
	 * 2. 含有 . 而非 .json，排除掉。
	 * </p>
	 * @param path 路径
	 * @return 是否排除
	 */
	boolean excluded(String path);
}
