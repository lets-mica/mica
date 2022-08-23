/*
 * Copyright (c) 2019-2029, 浪漫的收藏家 3385873384@qq.com.
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

package net.dreamlu.mica.swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * Swagger 配置
 *
 * @author 浪漫的收藏家
 */
@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties(MicaSwaggerProperties.class)
public class SwaggerConfiguration {

	/**
	 * 文档地址：
	 * <p>
	 * <a href="https://springdoc.org/#migrating-from-springfox">migrating-from-springfox</a>
	 * <p>
	 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md">OpenAPI-Specification</a>
	 */
	@Bean
	@ConditionalOnMissingBean(OpenAPI.class)
	public OpenAPI openAPI(Environment environment, MicaSwaggerProperties properties,
						   ObjectProvider<SwaggerCustomizer> customizerObjectProvider) {
		// 1. 组名为应用名
		String appName = environment.getProperty("spring.application.name");
		String defaultName = (appName == null ? "" : appName) + "服务";
		// title
		String title = Optional.ofNullable(properties.getTitle()).orElse(defaultName);
		// description
		String description = Optional.ofNullable(properties.getDescription()).orElse(defaultName);
		OpenAPI openAPI = new OpenAPI()
			.info(new Info()
				.title(title)
				.description(description)
				.version(properties.getVersion())
				.license(properties.getLicense())
			)
			.externalDocs(properties.getExternalDocumentation());
		// 自定义 customizer 配置
		customizerObjectProvider.orderedStream().forEach(customizer -> customizer.customize(openAPI));
		return openAPI;
	}

}
