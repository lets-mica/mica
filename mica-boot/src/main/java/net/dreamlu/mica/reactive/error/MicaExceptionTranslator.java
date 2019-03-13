package net.dreamlu.mica.reactive.error;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.exception.ServiceException;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.ObjectUtil;
import net.dreamlu.mica.reactive.filter.ReactiveRequestContextHolder;
import net.dreamlu.mica.common.error.MicaErrorEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

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
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class MicaExceptionTranslator {
	private final ApplicationEventPublisher publisher;

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<R<Object>> handleError(ServiceException e) {
		log.error("业务异常", e);
		R<Object> result = e.getResult();
		if (result != null) {
			return Mono.just(result);
		}
		// 发送：未知异常异常事件
		return ReactiveRequestContextHolder.getRequest()
			.doOnSuccess(r -> publishEvent(r, e))
			.flatMap(r -> Mono.just(R.fail(SystemCode.FAILURE, e.getMessage())));
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<?> handleError(Throwable e) {
		log.error("未知异常", e);
		// 发送：未知异常异常事件
		return ReactiveRequestContextHolder.getRequest()
			.doOnSuccess(r -> publishEvent(r, e))
			.flatMap(r -> Mono.just(R.fail(SystemCode.FAILURE)));
	}

	private void publishEvent(ServerHttpRequest request, Throwable error) {
		String requestUrl = request.getPath().pathWithinApplication().value();
		MicaErrorEvent event = new MicaErrorEvent();
		event.setRequestUrl(requestUrl);
		event.setStackTrace(Exceptions.getStackTraceAsString(error));
		event.setExceptionName(error.getClass().getName());
		event.setMessage(error.getMessage());
		event.setCreatedAt(LocalDateTime.now());
		StackTraceElement[] elements = error.getStackTrace();
		if (ObjectUtil.isNotEmpty(elements)) {
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
