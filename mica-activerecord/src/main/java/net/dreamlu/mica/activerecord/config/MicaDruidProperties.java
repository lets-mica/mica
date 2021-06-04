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

package net.dreamlu.mica.activerecord.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * druid 连接池配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("mica.druid")
public class MicaDruidProperties {

	private String url;
	private String username;
	private String password;
	private String publicKey;
	private String driverClass = null;
	private int initialSize = 1;
	private int minIdle = 10;
	private int maxActive = 32;
	private long maxWait = -1L;
	private long timeBetweenEvictionRunsMillis = 60000L;
	private long minEvictableIdleTimeMillis = 1800000L;
	private long timeBetweenConnectErrorMillis = 30000L;
	private String validationQuery = "select 1";
	private String connectionInitSql = null;
	private String connectionProperties = null;
	private boolean testWhileIdle = true;
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;
	private boolean removeAbandoned = false;
	private long removeAbandonedTimeoutMillis = 300000L;
	private boolean logAbandoned = false;
	private int maxPoolPreparedStatementPerConnectionSize = -1;
	private Integer defaultTransactionIsolation = null;
	private Integer validationQueryTimeout = null;
	private Integer timeBetweenLogStatsMillis = null;
	private Boolean keepAlive = null;
	private String filters;
	private boolean showSql = true;

}
