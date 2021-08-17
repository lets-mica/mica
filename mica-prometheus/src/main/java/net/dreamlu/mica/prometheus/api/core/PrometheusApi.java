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

package net.dreamlu.mica.prometheus.sd;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.auto.annotation.AutoIgnore;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * prometheus http sd
 *
 * @author L.cm
 */
@AutoIgnore
@RestController
@RequestMapping("actuator/prometheus")
@RequiredArgsConstructor
public class PrometheusApi {
	private final DiscoveryClient discoveryClient;

	@GetMapping("sd")
	public List<TargetGroup> getList() {
		List<String> serviceIdList = discoveryClient.getServices();
		if (serviceIdList == null || serviceIdList.isEmpty()) {
			return Collections.emptyList();
		}
		List<TargetGroup> targetGroupList = new ArrayList<>();
		for (String serviceId : serviceIdList) {
			List<ServiceInstance> instanceList = discoveryClient.getInstances(serviceId);
			List<String> targets = new ArrayList<>();
			for (ServiceInstance instance : instanceList) {
				targets.add(String.format("%s:%d", instance.getHost(), instance.getPort()));
			}
			Map<String, String> labels = new HashMap<>(2);
			labels.put("__meta_prometheus_job", serviceId);
			targetGroupList.add(new TargetGroup(targets, labels));
		}
		return targetGroupList;
	}

}
