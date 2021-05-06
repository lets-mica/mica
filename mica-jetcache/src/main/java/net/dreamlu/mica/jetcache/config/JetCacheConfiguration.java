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

package net.dreamlu.mica.jetcache.config;

import com.alicp.jetcache.anno.support.ConfigMap;
import com.alicp.jetcache.anno.support.DefaultSpringEncoderParser;
import com.alicp.jetcache.anno.support.DefaultSpringKeyConvertorParser;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.jetcache.jackson.JacksonKeyConvertor;
import net.dreamlu.mica.jetcache.jackson.JacksonValueDecoder;
import net.dreamlu.mica.jetcache.jackson.JacksonValueEncoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jetcache 配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(JetCacheAutoConfiguration.class)
public class JetCacheConfiguration implements InitializingBean {
	private ObjectMapper cacheMapper;

	@Bean("jacksonKeyConvertor")
	public JacksonKeyConvertor jacksonKeyConvertor() {
		return new JacksonKeyConvertor(JsonUtil.getInstance());
	}

	@Bean("jacksonValueDecoder")
	public JacksonValueDecoder jacksonValueDecoder() {
		return new JacksonValueDecoder(cacheMapper);
	}

	@Bean("jacksonValueEncoder")
	public JacksonValueEncoder jacksonValueEncoder() {
		return new JacksonValueEncoder(cacheMapper);
	}

	@Bean
	@ConditionalOnMissingBean
	public ConfigMap configMap() {
		return new ConfigMap();
	}

	@Bean
	public SpringConfigProvider springConfigProvider(ApplicationContext applicationContext) {
		DefaultSpringKeyConvertorParser convertorParser = new DefaultSpringKeyConvertorParser();
		convertorParser.setApplicationContext(applicationContext);
		SpringConfigProvider springConfigProvider = new SpringConfigProvider();
		springConfigProvider.setKeyConvertorParser(convertorParser);
		springConfigProvider.setApplicationContext(applicationContext);
		DefaultSpringEncoderParser encoderParser = new DefaultSpringEncoderParser();
		encoderParser.setApplicationContext(applicationContext);
		springConfigProvider.setEncoderParser(encoderParser);
		return springConfigProvider;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ObjectMapper mapper = JsonUtil.getInstance().copy();
		mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator());
		this.cacheMapper = mapper;
	}

}
