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

package net.dreamlu.mica.nats.config;

import io.nats.client.*;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import io.nats.client.support.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.nats.core.NatsStreamListenerDetector;
import net.dreamlu.mica.nats.utils.StreamConfigurationUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * nats 配置
 *
 * @author L.cm
 */
@Slf4j
@AutoConfiguration(after = NatsConfiguration.class)
@ConditionalOnProperty(
	prefix = NatsStreamProperties.PREFIX,
	name = "enable",
	havingValue = "true"
)
@ConditionalOnClass(Options.class)
public class NatsStreamConfiguration {

	@Bean
	public JetStream natsJetStream(Connection natsConnection,
								   NatsStreamProperties properties,
								   ObjectProvider<NatsStreamCustomizer> natsStreamCustomizerObjectProvider)
		throws IOException, JetStreamApiException {
		// stream 配置
		StreamConfiguration streamConfiguration = StreamConfigurationUtil.from(properties, natsStreamCustomizerObjectProvider);
		// stream 流管理器
		JetStreamManagement jsm = natsConnection.jetStreamManagement();
		StreamInfo streamInfo = jsm.addStream(streamConfiguration);
		// 打印 stream 信息
		log.info(JsonUtils.getFormatted(streamInfo));
		return natsConnection.jetStream();
	}

	@Bean
	public NatsStreamListenerDetector natsStreamListenerDetector(NatsStreamProperties properties,
																 ObjectProvider<NatsStreamCustomizer> natsStreamCustomizerObjectProvider,
																 Connection natsConnection,
																 JetStream natsJetStream) {
		return new NatsStreamListenerDetector(properties, natsStreamCustomizerObjectProvider, natsConnection, natsJetStream);
	}

}
