package net.dreamlu.mica.logging.appender;

/**
 * Appender 枚举
 *
 * @author L.cm
 */
public enum Appender {

	/**
	 * 控制台
	 */
	CONSOLE,
	/**
	 * 文件
	 */
	FILE,
	/**
	 * json 文件
	 */
	FILE_JSON,
	/**
	 * logstash
	 */
	LOG_STASH,
	/**
	 * loki
	 */
	LOKI;

}
