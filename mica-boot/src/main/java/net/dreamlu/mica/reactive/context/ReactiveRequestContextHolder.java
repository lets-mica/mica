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

package net.dreamlu.mica.reactive.context;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * ReactiveRequestContextHolder
 *
 * @author L.cm
 */
public class ReactiveRequestContextHolder {
	static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

	/**
	 * Gets the {@code Mono<ServerHttpRequest>} from Reactor {@link Context}
	 * @return the {@code Mono<ServerHttpRequest>}
	 */
	public static Mono<ServerHttpRequest> getRequest() {
		return Mono.subscriberContext()
			.map(ctx -> ctx.get(CONTEXT_KEY))
			.cast(ServerHttpRequest.class);
	}

}
