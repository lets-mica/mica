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

package net.dreamlu.mica.core.constant;


/**
 * mica 常量
 *
 * @author L.cm
 */
public interface MicaConstant {

	/**
	 * Spring 应用名 prop key
	 */
	String SPRING_APP_NAME_KEY = "spring.application.name";

	/**
	 * mica env key
	 */
	String MICA_ENV_KEY = "mica.env";

	/**
	 * 判断是否开发环境的 key
	 */
	String MICA_IS_LOCAL_KEY = "mica.is-local";

	/**
	 * mdc request id key
	 */
	String MDC_REQUEST_ID_KEY = "requestId";

	/**
	 * mdc account id key
	 */
	String MDC_ACCOUNT_ID_KEY = "accountId";

	/**
	 * request id key
	 */
	String REQUEST_ID_KEY = "mica_request_id";

}
