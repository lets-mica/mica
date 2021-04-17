package net.dreamlu.mica.lite.error;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.context.DefaultMicaContext;
import net.dreamlu.mica.core.context.IMicaContext;
import net.dreamlu.mica.core.exception.ServiceException;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.core.utils.WebUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * mica 未知异常转译和发送，方便监听，对未知异常统一处理。Order 排序优先级低
 *
 * @author L.cm
 */
@Slf4j
@Order
@RestControllerAdvice
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MicaExceptionTranslator {
	private final IMicaContext micaContext;
	private final ApplicationEventPublisher publisher;

	@Autowired
	public MicaExceptionTranslator(ObjectProvider<IMicaContext> micaContextProvider,
								   ApplicationEventPublisher publisher) {
		this.micaContext = micaContextProvider.getIfAvailable(DefaultMicaContext::new);
		this.publisher = publisher;
	}

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
		// 服务异常类型
		event.setErrorType(ErrorType.REQUEST);
		// 异步获取不到的一些信息
		event.setRequestId(micaContext.getRequestId());
		HttpServletRequest request = WebUtil.getRequest();
		// 请求方法名
		event.setRequestMethod(request.getMethod());
		// 拼接地址
		String requestUrl = request.getRequestURI();
		String queryString = request.getQueryString();
		if (StringUtil.isNotBlank(queryString)) {
			requestUrl = requestUrl + StringPool.QUESTION_MARK + queryString;
		}
		// 请求ip
		event.setRequestIp(WebUtil.getIP(request));
		event.setRequestUrl(requestUrl);
		// 堆栈信息
		ErrorUtil.initErrorInfo(error, event);
		// 发布事件，其他参数可监听时异步获取
		publisher.publishEvent(event);
	}

}
