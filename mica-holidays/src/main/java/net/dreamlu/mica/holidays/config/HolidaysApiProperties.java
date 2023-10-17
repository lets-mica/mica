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

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * HolidaysApi 配置类
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(HolidaysApiProperties.PREFIX)
public class HolidaysApiProperties {
	public static final String PREFIX = "mica.holidays";

	/**
	 * 自行扩展的 json 文件路径
	 */
	private List<ExtData> extData = new ArrayList<>();

	@Getter
	@Setter
	public static class ExtData {
		/**
		 * 年份
		 */
		private Integer year;
		/**
		 * 数据目录
		 */
		private String dataPath;
	}

}
