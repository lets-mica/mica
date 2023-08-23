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

package net.dreamlu.mica.nats.utils;

import io.nats.client.api.StreamConfiguration;
import lombok.experimental.UtilityClass;
import net.dreamlu.mica.nats.config.NatsStreamCustomizer;
import net.dreamlu.mica.nats.config.NatsStreamProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * nats Stream 配置 工具
 *
 * @author L.cm
 */
@UtilityClass
public class StreamConfigurationUtil {

	/**
	 * 构造 StreamConfiguration
	 *
	 * @param subject                            subject
	 * @param streamName                         stream name
	 * @param properties                         NatsStreamProperties
	 * @param natsStreamCustomizerObjectProvider ObjectProvider
	 * @return StreamConfiguration
	 */
	public static StreamConfiguration from(String subject,
										   String streamName,
										   NatsStreamProperties properties,
										   ObjectProvider<NatsStreamCustomizer> natsStreamCustomizerObjectProvider) {
		StreamConfiguration.Builder builder = StreamConfiguration.builder()
			.name(StringUtils.hasText(streamName) ? streamName : properties.getName())
			.description(properties.getDescription())
			.subjects(Collections.singletonList(subject))
			.retentionPolicy(properties.getRetentionPolicy())
			.maxConsumers(properties.getMaxConsumers())
			.maxMessages(properties.getMaxMsgs())
			.maxMessagesPerSubject(properties.getMaxMsgsPerSubject())
			.maxBytes(properties.getMaxBytes())
			.maxAge(properties.getMaxAge())
			.maxMsgSize(properties.getMaxMsgSize())
			.storageType(properties.getStorageType())
			.replicas(properties.getReplicas())
			.noAck(properties.isNoAck())
			.templateOwner(properties.getTemplateOwner())
			.discardPolicy(properties.getDiscardPolicy())
			.discardNewPerSubject(properties.isDiscardNewPerSubject())
			.duplicateWindow(properties.getDuplicateWindow())
			.allowRollup(properties.isAllowRollup())
			.allowDirect(properties.isAllowDirect())
			.denyDelete(properties.isDenyDelete())
			.denyPurge(properties.isDenyPurge())
			.metadata(properties.getMetadata());
		// 是否已封存
		if (properties.isSealed()) {
			builder.seal();
		}
		// 用户自定义配置
		natsStreamCustomizerObjectProvider.orderedStream().forEach(natsOptionsCustomizer -> natsOptionsCustomizer.customize(builder));
		return builder.build();
	}

}
