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

package net.dreamlu.mica.logging.appender;

import ch.qos.logback.classic.LoggerContext;

/**
 * logging Appender 抽象
 *
 * @author L.cm
 */
public interface ILoggingAppender {

	/**
	 * 启动
	 *
	 * @param context LoggerContext
	 */
	void start(LoggerContext context);

	/**
	 * 重置
	 *
	 * @param context LoggerContext
	 */
	void reset(LoggerContext context);

}
