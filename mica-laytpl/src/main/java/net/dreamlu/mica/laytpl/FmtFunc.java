/*
 * *************************************************************************
 *   Copyright (c) 2018-2025, dreamlu.net All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the dreamlu.net developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: chunmeng.lu (qq596392912@gmail.com)
 * *************************************************************************
 */

package net.dreamlu.mica.laytpl;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.DateUtil;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 内置函数
 *
 * 提供给 tpl 和 Thymeleaf 使用
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class FmtFunc {
	private final MicaLayTplProperties properties;

	/**
	 * 对象格式化
	 * @param object 格式化对象
	 * @return 格式化后的字符串
	 */
	public String format(Object object) {
		if (object instanceof Number) {
			return format(object, properties.getNumPattern());
		} else if (object instanceof Date) {
			return format(object, properties.getDatePattern());
		} else if (object instanceof LocalTime) {
			return format(object, properties.getLocalTimePattern());
		} else if (object instanceof LocalDate) {
			return format(object, properties.getLocalDatePattern());
		} else if (object instanceof LocalDateTime) {
			return format(object, properties.getLocalDateTimePattern());
		}
		throw new MicaTplException("未支持的对象格式" + object);
	}

	/**
	 * 对象格式化
	 * @param object 格式化对象
	 * @param pattern 表达式
	 * @return 格式化后的字符串
	 */
	public String format(Object object, String pattern) {
		if (object instanceof Number) {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			return decimalFormat.format(object);
		} else if (object instanceof Date) {
			return DateUtil.format((Date) object, pattern);
		} else if (object instanceof TemporalAccessor) {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
			return df.format((TemporalAccessor) object);
		}
		throw new MicaTplException("未支持的对象格式" + object);
	}

}
