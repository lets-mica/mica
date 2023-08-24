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

package net.dreamlu.mica.lite.config;

import net.dreamlu.mica.core.spring.SpringContextUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * mica lite 配置
 *
 * @author L.cm
 */
@AutoConfiguration
public class MicaLiteConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SpringContextUtil springContextUtil() {
		return new SpringContextUtil();
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizerEnableDefaultViewInclusion() {
		// 支持 R 返回 json 渲染，开启默认的 Jackson 会将所有属性包含在序列化或反序列化的结果中，无论是否定义了视图。
		return builder -> builder.defaultViewInclusion(true);
	}

}
