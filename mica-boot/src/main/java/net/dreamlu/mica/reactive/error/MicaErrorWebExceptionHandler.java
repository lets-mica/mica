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
import net.dreamlu.mica.core.exception.ServiceException;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * webflux 异常处理
 *
 * @author L.cm
 */
@Slf4j
public class MicaErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public MicaErrorWebExceptionHandler(ErrorAttributes attributes, ResourceProperties properties,
		ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(attributes, properties, errorProperties, applicationContext);
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	/**
	 * Render the error information as a JSON payload.
	 * @param request the current request
	 * @return a {@code Publisher} of the HTTP response
	 */
	@Override
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Throwable error = this.getError(request);
		HttpStatus status = determineHttpStatus(error);
		// 拼接地址
		MultiValueMap<String, String> queryParams = request.queryParams();
		String requestUrl = UriComponentsBuilder.fromPath(request.path()).queryParams(queryParams).build().toUriString();
		log.error(String.format("URL:%s error status:%d", requestUrl, status.value()), error);
		// 返回消息
		String message = status.value() + ":" + status.getReasonPhrase();
		R<Object> result;
		if (error instanceof ServiceException) {
			result = ((ServiceException) error).getResult();
			result = Optional.ofNullable(result).orElse(R.fail(SystemCode.FAILURE));
		} else {
			result = R.fail(SystemCode.FAILURE, message);
		}
		return ServerResponse.status(status)
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body(BodyInserters.fromObject(result));
	}

	private HttpStatus determineHttpStatus(Throwable error) {
		if (error instanceof ResponseStatusException) {
			return ((ResponseStatusException)error).getStatus();
		} else {
			ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(error.getClass(), ResponseStatus.class);
			return responseStatus != null ? responseStatus.code() : HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}
