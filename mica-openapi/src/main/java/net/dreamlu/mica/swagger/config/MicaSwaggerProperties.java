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

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.License;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.lang.Nullable;

/**
 * Swagger 配置
 *
 * @author 浪漫的收藏家
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(MicaSwaggerProperties.PREFIX)
public class MicaSwaggerProperties {
	public static final String PREFIX = "mica.swagger";

	/**
	 * 标题，默认：XXX服务
	 */
	@Nullable
	private String title;
	/**
	 * 详情，默认：XXX服务
	 */
	@Nullable
	private String description;
	/**
	 * 版本号，默认：V1.0
	 */
	private String version = "V1.0";
	/**
	 * 协议
	 */
	@NestedConfigurationProperty
	private License license;
	/**
	 * 扩展文档
	 */
	@NestedConfigurationProperty
	private ExternalDocumentation externalDocumentation;

}
