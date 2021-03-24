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
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.logging.config.MicaLoggingProperties;

/**
 * json 日志输出，json 日志只输出 all.log
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class LoggingJsonFileAppender implements ILoggingAppender {
	private final MicaLoggingProperties properties;

	@Override
	public void start(LoggerContext context) {

	}

	@Override
	public void reset(LoggerContext context) {

	}
}
