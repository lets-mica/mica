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

package net.dreamlu.mica.http;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.launcher.MicaLogLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.concurrent.TimeUnit;

/**
 * http 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("mica.http")
public class MicaHttpProperties {
	/**
	 * 最大连接数，默认：200
	 */
	private int maxConnections = 200;
	/**
	 * 连接存活时间，默认：900L
	 */
	private long timeToLive = 900L;
	/**
	 * 连接池存活时间单位，默认：秒
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	/**
	 * 链接超时，默认：2000毫秒
	 */
	private int connectionTimeout = 2000;
	/**
	 * 是否支持重定向，默认：true
	 */
	private boolean followRedirects = true;
	/**
	 * 关闭证书校验
	 */
	private boolean disableSslValidation = true;
	/**
	 * 日志级别
	 */
	private MicaLogLevel level = MicaLogLevel.NONE;
}
