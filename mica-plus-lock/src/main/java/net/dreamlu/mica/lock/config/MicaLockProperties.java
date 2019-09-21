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

package net.dreamlu.mica.lock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 分布式锁配置
 *
 * @author L.cm
 */
@Getter
@Setter
@ConfigurationProperties(MicaLockProperties.PREFIX)
public class MicaLockProperties {
	public static final String PREFIX = "mica.lock";

	/**
	 * 是否开启：默认为：true，便于生成配置提示。
	 */
	private Boolean enabled = Boolean.TRUE;
	/**
	 * 单机配置：redis 服务地址
	 */
	private String address = "redis://127.0.0.1:6379";
	/**
	 * 密码配置
	 */
	private String password;
	/**
	 * db
	 */
	private Integer database = 0;
	/**
	 * 连接池大小
	 */
	private Integer poolSize = 20;
	/**
	 * 最小空闲连接数
	 */
	private Integer idleSize = 5;
	/**
	 * 连接空闲超时，单位：毫秒
	 */
	private Integer idleTimeout = 60000;
	/**
	 * 连接超时，单位：毫秒
	 */
	private Integer connectionTimeout = 3000;
	/**
	 * 命令等待超时，单位：毫秒
	 */
	private Integer timeout = 10000;
	/**
	 * 集群模式，单机：single，主从：master，哨兵模式：sentinel，集群模式：cluster
	 */
	private Mode mode = Mode.single;
	/**
	 * 主从模式，主地址
	 */
	private String masterAddress;
	/**
	 * 主从模式，从地址
	 */
	private String[] slaveAddress;
	/**
	 * 哨兵模式：主名称
	 */
	private String masterName;
	/**
	 * 哨兵模式地址
	 */
	private String[] sentinelAddress;
	/**
	 * 集群模式节点地址
	 */
	private String[] nodeAddress;

	public enum Mode {
		/**
		 * 集群模式，单机：single，主从：master，哨兵模式：sentinel，集群模式：cluster
		 */
		single,
		master,
		sentinel,
		cluster
	}
}
