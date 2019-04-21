package net.dreamlu.mica.servlet.error;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.error.MicaErrorEvent;
import net.dreamlu.mica.core.exception.ServiceException;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import net.dreamlu.mica.core.utils.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * mica 未知异常转译和发送，方便监听，对未知异常统一处理。Order 排序优先级低
 *
 * @author L.cm
 */
@Slf4j
@Order
@Configuration
@RestControllerAdvice
@AllArgsConstructor
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MicaExceptionTranslator {
	private final ApplicationEventPublisher publisher;

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Object> handleError(ServiceException e) {
		log.error("业务异常", e);
		R<Object> result = e.getResult();
		if (result == null) {
			// 发送：未知业务异常事件
			result = R.fail(SystemCode.FAILURE, e.getMessage());
			publishEvent(e);
		}
		return result;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Object> handleError(Throwable e) {
		log.error("未知异常", e);
		// 发送：未知异常异常事件
		publishEvent(e);
		return R.fail(SystemCode.FAILURE);
	}

	private void publishEvent(Throwable error) {
		MicaErrorEvent event = new MicaErrorEvent();
		HttpServletRequest request = WebUtil.getRequest();
		// 请求方法名
		event.setRequestMethod(request.getMethod());
		// 拼接地址
		String requestUrl = request.getRequestURI();
		String queryString = request.getQueryString();
		if (StringUtil.isNotBlank(queryString)) {
			requestUrl = requestUrl + StringPool.QUESTION_MARK + queryString;
		}
		event.setRequestUrl(requestUrl);
		// 堆栈信息
		event.setStackTrace(Exceptions.getStackTraceAsString(error));
		event.setExceptionName(error.getClass().getName());
		event.setMessage(error.getMessage());
		event.setCreatedAt(LocalDateTime.now());
		StackTraceElement[] elements = error.getStackTrace();
		if (ObjectUtil.isNotEmpty(elements)) {
			// 报错的类信息
			StackTraceElement element = elements[0];
			event.setClassName(element.getClassName());
			event.setFileName(element.getFileName());
			event.setMethodName(element.getMethodName());
			event.setLineNumber(element.getLineNumber());
		}
		// 发布事件
		publisher.publishEvent(event);
	}
}
