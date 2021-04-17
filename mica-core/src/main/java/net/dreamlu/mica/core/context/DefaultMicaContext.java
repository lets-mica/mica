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

import java.util.function.Function;

/**
 * 上下文，默认给个空的
 *
 * @author L.cm
 */
public class DefaultMicaContext implements IMicaContext {

	@Override
	public String getRequestId() {
		return null;
	}

	@Override
	public String getTenantId() {
		return null;
	}

	@Override
	public String getAccountId() {
		return null;
	}

	@Override
	public String get(String ctxKey) {
		return null;
	}

	@Override
	public <T> T get(String ctxKey, Function<String, T> function) {
		return null;
	}

}
