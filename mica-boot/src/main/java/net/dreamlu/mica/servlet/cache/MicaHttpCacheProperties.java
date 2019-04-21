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

package net.dreamlu.mica.servlet.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Http Cache 配置
 *
 * @author L.cm
 */
@RefreshScope
@ConfigurationProperties("mica.http.cache")
public class MicaHttpCacheProperties {

	/**
	 * Http-cache 的 spring cache名，默认：micaHttpCache
	 */
	@Getter
	@Setter
	private String cacheName = "micaHttpCache";

	/**
	 * 默认拦截/**
	 */
	@Getter
	private final List<String> includePatterns = new ArrayList<String>(){{ add("/**"); }};

	/**
	 * 默认排除静态文件目录
	 */
	@Getter
	private final List<String> excludePatterns = new ArrayList<>();
}
