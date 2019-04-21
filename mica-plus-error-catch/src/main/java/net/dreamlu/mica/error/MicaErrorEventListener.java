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

package net.dreamlu.mica.error;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.error.MicaErrorEvent;
import net.dreamlu.mica.props.MicaProperties;
import net.dreamlu.mica.server.ServerInfo;
import net.dreamlu.mica.stream.ServiceErrorStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;

/**
 * mica 异常监听
 *
 * @author L.cm
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableBinding(ServiceErrorStreams.class)
public class MicaErrorEventListener {

	private final ServerInfo serverInfo;
	private final MicaProperties micaProperties;
	private final ServiceErrorStreams serviceErrorStreams;

	@Async
	@EventListener(MicaErrorEvent.class)
	public void handleError(MicaErrorEvent event) {
		log.info("[Error Event] Listen to the [MicaErrorEvent] exception: {}", event.getExceptionName());
		// 设置 remoteHost 触发的服务 host 和 ip 信息
		String hostname = serverInfo.getHostName();
		String ipWithPort = serverInfo.getIpWithPort();
		// 规则 hostname[ip:port]
		event.setRemoteHost(String.format("%s[%s]", hostname, ipWithPort));
		// 应用名
		event.setAppName(micaProperties.getName());
		// 环境
		event.setEnv(micaProperties.getEnv());
		// 发送 stream 事件
		boolean result = serviceErrorStreams.messageChannel().send(MessageBuilder.withPayload(event).build());
		log.info("[Error Event] Send stream result: {}", result);
	}

}
