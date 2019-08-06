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

package net.dreamlu.mica.ribbon.predicate;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import net.dreamlu.mica.config.SpringUtils;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.ribbon.support.MicaRibbonRuleProperties;

import java.util.Map;

/**
 * 基于 Metadata 的服务筛选
 *
 * @author L.cm
 */
public class MetadataAwarePredicate extends DiscoveryEnabledPredicate {
	public static final MetadataAwarePredicate INSTANCE = new MetadataAwarePredicate();

	@Override
	protected boolean apply(NacosServer server) {
		final Map<String, String> metadata = server.getMetadata();

		// 获取配置
		MicaRibbonRuleProperties properties = SpringUtils.getBean(MicaRibbonRuleProperties.class);
		// 服务里的配置
		String localTag = properties.getTag();

		if (StringUtil.isBlank(localTag)) {
			return true;
		}

		// 本地有 tag，服务没有，返回 false
		String metaDataTag = metadata.get("tag");
		if (StringUtil.isBlank(metaDataTag)) {
			return false;
		}

		return metaDataTag.equals(localTag);
	}

}
