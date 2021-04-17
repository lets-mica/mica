/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.lite.error;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import net.dreamlu.mica.core.utils.StringUtil;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理，处理可预见的异常，Order 排序优先级高
 *
 * <p>
 * Bean-Violation 异常处理
 * </p>
 *
 * @author L.cm
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RestExceptionTranslator {

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(MissingServletRequestParameterException e) {
		log.warn("缺少请求参数:{}", e.getMessage());
		String message = String.format("缺少必要的请求参数: %s", e.getParameterName());
		return R.fail(SystemCode.PARAM_MISS, message);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(MethodArgumentTypeMismatchException e) {
		log.warn("请求参数格式错误:{}", e.getMessage());
		String message = String.format("请求参数格式错误: %s", e.getName());
		return R.fail(SystemCode.PARAM_TYPE_ERROR, message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(MethodArgumentNotValidException e) {
		log.warn("参数验证失败:{}", e.getMessage());
		return handleError(e.getBindingResult());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(BindException e) {
		log.warn("参数绑定失败:{}", e.getMessage());
		return handleError(e.getBindingResult());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(ConstraintViolationException e) {
		log.warn("参数验证失败:{}", e.getMessage());
		return handleError(e.getConstraintViolations());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public R<Object> handleError(NoHandlerFoundException e) {
		log.error("404没找到请求:{}", e.getMessage());
		return R.fail(SystemCode.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<Object> handleError(HttpMessageNotReadableException e) {
		log.error("消息不能读取:{}", e.getMessage());
		return R.fail(SystemCode.MSG_NOT_READABLE, e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public R<Object> handleError(HttpRequestMethodNotSupportedException e) {
		log.error("不支持当前请求方法:{}", e.getMessage());
		return R.fail(SystemCode.METHOD_NOT_SUPPORTED, e.getMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public R<Object> handleError(HttpMediaTypeNotSupportedException e) {
		log.error("不支持当前媒体类型:{}", e.getMessage());
		return R.fail(SystemCode.MEDIA_TYPE_NOT_SUPPORTED, e.getMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public R<Object> handleError(HttpMediaTypeNotAcceptableException e) {
		String message = e.getMessage() + " " + StringUtil.join(e.getSupportedMediaTypes());
		log.error("不接受的媒体类型:{}", message);
		return R.fail(SystemCode.MEDIA_TYPE_NOT_SUPPORTED, message);
	}

	/**
	 * 处理 BindingResult
	 *
	 * @param result BindingResult
	 * @return R
	 */
	private static R<Object> handleError(BindingResult result) {
		FieldError error = result.getFieldError();
		String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
		return R.fail(SystemCode.PARAM_BIND_ERROR, message);
	}

	/**
	 * 处理 ConstraintViolation
	 *
	 * @param violations 校验结果
	 * @return R
	 */
	private static R<Object> handleError(Set<ConstraintViolation<?>> violations) {
		ConstraintViolation<?> violation = violations.iterator().next();
		String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
		String message = String.format("%s:%s", path, violation.getMessage());
		return R.fail(SystemCode.PARAM_VALID_ERROR, message);
	}

}
