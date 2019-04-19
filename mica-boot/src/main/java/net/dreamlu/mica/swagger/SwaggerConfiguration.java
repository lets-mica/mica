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

package net.dreamlu.mica.swagger;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.props.MicaProperties;
import net.dreamlu.mica.props.MicaSwaggerProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Swagger2配置
 *
 * @author L.cm
 */
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(MicaSwaggerProperties.class)
@ConditionalOnClass(Docket.class)
@ConditionalOnProperty(value = "mica.swagger.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingClass("org.springframework.cloud.gateway.config.GatewayAutoConfiguration")
public class SwaggerConfiguration {
	private final MicaProperties micaProperties;
	private final MicaSwaggerProperties swaggerProperties;

	@Bean
	public Docket createRestApi() {
		// 组名为应用名
		String appName = micaProperties.getName();
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
			.useDefaultResponseMessages(false)
			.globalOperationParameters(globalHeaders())
			.apiInfo(apiInfo(appName)).select()
			.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
			.paths(PathSelectors.any())
			.build();
		// 如果开启认证
		if (swaggerProperties.getAuthorization().getEnabled()) {
			docket.securitySchemes(Collections.singletonList(apiKey()));
			docket.securityContexts(Collections.singletonList(securityContext()));
		}
		return docket;
	}

	/**
	 * 配置基于 ApiKey 的鉴权对象
	 *
	 * @return {ApiKey}
	 */
	private ApiKey apiKey() {
		return new ApiKey(swaggerProperties.getAuthorization().getName(),
			swaggerProperties.getAuthorization().getKeyName(),
			ApiKeyVehicle.HEADER.getValue());
	}

	/**
	 * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
	 * 其中 securityReferences 为配置启用的鉴权策略
	 *
	 * @return {SecurityContext}
	 */
	private SecurityContext securityContext() {
		return SecurityContext.builder()
			.securityReferences(defaultAuth())
			.forPaths(PathSelectors.regex(swaggerProperties.getAuthorization().getAuthRegex()))
			.build();
	}

	/**
	 * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为ApiKey对象里面的name，保持一致才能开启全局鉴权
	 *
	 * @return {List<SecurityReference>}
	 */
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Collections.singletonList(SecurityReference.builder()
			.reference(swaggerProperties.getAuthorization().getName())
			.scopes(authorizationScopes).build());
	}

	private ApiInfo apiInfo(String appName) {
		String defaultName = appName + " 服务";
		String title = Optional.ofNullable(swaggerProperties.getTitle())
			.orElse(defaultName);
		String description = Optional.ofNullable(swaggerProperties.getDescription())
			.orElse(defaultName);
		return new ApiInfoBuilder()
			.title(title)
			.description(description)
			.version(swaggerProperties.getVersion())
			.contact(new Contact(swaggerProperties.getContactUser(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
			.build();
	}

	private List<Parameter> globalHeaders() {
		List<Parameter> pars = new ArrayList<>();
		swaggerProperties.getHeaders().forEach(header -> {
			Parameter parameter = new ParameterBuilder()
				.name(header.getName())
				.description(header.getDescription())
				.modelRef(new ModelRef("string")).parameterType("header")
				.required(header.isRequired())
				.build();
			pars.add(parameter);
		});
		return pars;
	}

}
