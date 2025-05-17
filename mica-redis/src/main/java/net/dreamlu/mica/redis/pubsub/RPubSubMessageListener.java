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

package net.dreamlu.mica.redis.pubsub;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

import java.util.List;

/**
 * 消息监听器
 *
 * @see org.springframework.data.redis.listener.adapter.MessageListenerAdapter
 */
public interface RPubSubMessageListener extends MessageListener {

	/**
	 * 获取订阅的 topic
	 *
	 * @return topic
	 * @see org.springframework.data.redis.listener.ChannelTopic
	 * @see org.springframework.data.redis.listener.PatternTopic
	 */
	List<Topic> getTopics();
}
