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

package net.dreamlu.mica.actuator.druid;

import com.alibaba.druid.stat.JdbcStatManager;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import javax.management.JMException;
import javax.management.openmbean.TabularData;

/**
 * druid stat 端点
 *
 * @author L.cm
 */
@Endpoint(id = "druid")
public class DruidEndpoint {

	@ReadOperation
	public TabularData jdbcStat() throws JMException {
		JdbcStatManager instance = JdbcStatManager.getInstance();
		return instance.getSqlList();
	}

}
