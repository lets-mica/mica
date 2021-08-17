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
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * 告警模型
 *
 * @author L.cm
 */
@Data
public class AlertInfo implements Serializable {

	/**
	 * 状态 resolved|firing
	 */
	private String status;
	/**
	 * 标签集合
	 */
	private Map<String, String> labels;
	/**
	 * 注释集合
	 */
	private Map<String, String> annotations;
	/**
	 * 开始时间
	 */
	private OffsetDateTime startsAt;
	/**
	 * 结束时间
	 */
	private OffsetDateTime endsAt;
	/**
	 * identifies the entity that caused the alert
	 */
	private String generatorURL;
	/**
	 * fingerprint to identify the alert
	 */
	private String fingerprint;

}
