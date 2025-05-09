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

package net.dreamlu.mica.core.context;

import org.jspecify.annotations.Nullable;

import java.util.function.Function;

/**
 * mica 微服务 上下文
 *
 * @author L.cm
 */
public interface IMicaContext {

	/**
	 * 获取 请求 id
	 *
	 * @return 请求id
	 */
	@Nullable
	String getRequestId();

	/**
	 * 获取租户id
	 *
	 * @return 租户id
	 */
	@Nullable
	default String getTenantId() {
		return null;
	}

	/**
	 * 账号id
	 *
	 * @return 账号id
	 */
	@Nullable
	String getAccountId();

	/**
	 * 微服务版本号，用于灰度
	 *
	 * @return 账号id
	 */
	@Nullable
	default String getVersion() {
		return null;
	}

	/**
	 * 获取上下文中的数据
	 *
	 * @param ctxKey 上下文中的key
	 * @return 返回对象
	 */
	@Nullable
	String get(String ctxKey);

	/**
	 * 获取上下文中的数据
	 *
	 * @param ctxKey   上下文中的key
	 * @param function 函数式
	 * @param <T>      泛型对象
	 * @return 返回对象
	 */
	@Nullable
	<T> T get(String ctxKey, Function<String, T> function);

}
