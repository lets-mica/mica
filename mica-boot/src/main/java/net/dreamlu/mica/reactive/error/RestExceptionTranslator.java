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

package net.dreamlu.mica.reactive.error;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.error.BaseExceptionTranslator;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

/**
 * @author L.cm
 * 异常信息处理
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class RestExceptionTranslator extends BaseExceptionTranslator {

	@ExceptionHandler(ServerWebInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<R<Object>> handleError(ServerWebInputException e) {
		log.error("缺少请求参数:{}", e.getMessage());
		MethodParameter parameter = e.getMethodParameter();
		String message = String.format("缺少必要的请求参数: %s", parameter.getParameterName());
		return Mono.just(R.fail(SystemCode.PARAM_MISS, message));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<R<Object>> handleError(MethodArgumentNotValidException e) {
		log.error("参数验证失败:{}", e.getMessage());
		return Mono.just(handleError(e.getBindingResult()));
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<R<Object>> handleError(BindException e) {
		log.error("参数绑定失败:{}", e.getMessage());
		return Mono.just(handleError(e.getBindingResult()));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<R<Object>> handleError(WebExchangeBindException e) {
		log.error("参数绑定失败:{}", e.getMessage());
		return Mono.just(handleError(e.getBindingResult()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<R<Object>> handleError(ConstraintViolationException e) {
		log.error("参数验证失败:{}", e.getMessage());
		return Mono.just(handleError(e.getConstraintViolations()));
	}

	@ExceptionHandler(ResponseStatusException.class)
	public Mono<ResponseEntity<R<Object>>> handleError(ResponseStatusException e) {
		log.error("响应状态异常:{}", e.getMessage());
		ResponseEntity<R<Object>> entity = ResponseEntity.status(e.getStatus())
			.body(R.fail(SystemCode.REQ_REJECT, e.getMessage()));
		return Mono.just(entity);
	}

}
