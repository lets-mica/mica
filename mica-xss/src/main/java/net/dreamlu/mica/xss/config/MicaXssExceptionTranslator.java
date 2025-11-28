package net.dreamlu.mica.xss.config;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import net.dreamlu.mica.xss.code.XssResultCode;
import net.dreamlu.mica.xss.core.FromXssException;
import net.dreamlu.mica.xss.core.JacksonXssException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * xss 异常处理
 *
 * @author L.cm
 */
@Slf4j
@Hidden
@RestControllerAdvice
@AutoConfiguration
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MicaXssExceptionTranslator {

	@ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(MethodArgumentConversionNotSupportedException e) {
		Throwable throwable = NestedExceptionUtils.getRootCause(e);
		if (throwable instanceof FromXssException) {
			log.error("Xss校验失败:{}", throwable.getMessage());
			return R.fail(XssResultCode.XSS_FORM_FAILED, throwable.getMessage());
		} else {
			log.error("消息不能读取，转换异常", throwable);
			return R.fail(SystemCode.MSG_NOT_READABLE);
		}
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(HttpMessageNotReadableException e) {
		Throwable throwable = NestedExceptionUtils.getRootCause(e);
		if (throwable instanceof JacksonXssException) {
			log.error("Xss校验失败:{}", throwable.getMessage());
			return R.fail(XssResultCode.XSS_JSON_FAILED, throwable.getMessage());
		} else {
			log.error("消息不能读取:{}", e.getMessage());
			return R.fail(SystemCode.MSG_NOT_READABLE);
		}
	}
}
