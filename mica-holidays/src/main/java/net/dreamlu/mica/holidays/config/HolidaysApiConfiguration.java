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

package net.dreamlu.mica.holidays.config;

import net.dreamlu.mica.holidays.core.HolidaysApi;
import net.dreamlu.mica.holidays.impl.HolidaysApiImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/**
 * 配置
 *
 * @author L.cm
 */
@AutoConfiguration
@EnableConfigurationProperties(HolidaysApiProperties.class)
public class HolidaysApiConfiguration {

	@Bean
	public HolidaysApi holidaysApi(ResourceLoader resourceLoader,
								   HolidaysApiProperties properties) {
		return new HolidaysApiImpl(resourceLoader, properties);
	}

}
