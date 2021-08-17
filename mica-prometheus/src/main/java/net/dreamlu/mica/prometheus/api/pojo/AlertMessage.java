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

package net.dreamlu.mica.prometheus.api.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * alert hook
 *
 * @author L.cm
 */
@Data
public class AlertMessage implements Serializable {

	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 由于 “max_alerts” 而截断了多少警报
	 */
	private Integer truncatedAlerts;
	/**
	 * 分组 key
	 */
	private String groupKey;
	/**
	 * 状态 resolved|firing
	 */
	private String status;
	/**
	 * 接收者
	 */
	private String receiver;
	/**
	 * 分组 labels
	 */
	private Map<String, String> groupLabels;
	/**
	 * 通用 label
	 */
	private Map<String, String> commonLabels;
	/**
	 * 通用注解
	 */
	private Map<String, String> commonAnnotations;
	/**
	 * 扩展 url 地址
	 */
	private String externalURL;
	/**
	 * alerts
	 */
	private List<AlertInfo> alerts;

}
