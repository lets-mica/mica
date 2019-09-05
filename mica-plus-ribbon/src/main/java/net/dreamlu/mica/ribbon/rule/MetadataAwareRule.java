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

package net.dreamlu.mica.ribbon.rule;

import com.netflix.loadbalancer.Server;
import net.dreamlu.mica.config.SpringUtils;
import net.dreamlu.mica.core.utils.INetUtil;
import net.dreamlu.mica.core.utils.ObjectUtil;
import net.dreamlu.mica.ribbon.predicate.MetadataAwarePredicate;
import net.dreamlu.mica.ribbon.support.MicaRibbonRuleProperties;
import org.springframework.util.PatternMatchUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ribbon 路由规则器
 *
 * @author dream.lu
 */
public class MetadataAwareRule extends DiscoveryEnabledRule {

	public MetadataAwareRule(boolean enableFallbackPredicate) {
		super(MetadataAwarePredicate.INSTANCE, enableFallbackPredicate);
	}

	@Override
	public List<Server> filterServers(List<Server> serverList) {
		MicaRibbonRuleProperties ribbonProperties = SpringUtils.getBean(MicaRibbonRuleProperties.class);
		List<String> priorIpPattern = ribbonProperties.getPriorIpPattern();

		// 1. 查找是否有本机 ip
		String hostIp = INetUtil.getHostIp();

		// 优先的 ip 规则
		boolean hasPriorIpPattern = !priorIpPattern.isEmpty();
		String[] priorIpPatterns = priorIpPattern.toArray(new String[0]);

		List<Server> priorServerList = new ArrayList<>();
		for (Server server : serverList) {
			String host = server.getHost();
			// 2. 优先本地 ip 的服务
			if (ObjectUtil.nullSafeEquals(hostIp, host)) {
				return Collections.singletonList(server);
			}
			// 3. 优先的 ip 服务
			if (hasPriorIpPattern && PatternMatchUtils.simpleMatch(priorIpPatterns, host)) {
				priorServerList.add(server);
			}
		}

		// 4. 如果优先的有数据直接返回
		if (!priorServerList.isEmpty()) {
			return priorServerList;
		}

		return Collections.unmodifiableList(serverList);
	}

}
