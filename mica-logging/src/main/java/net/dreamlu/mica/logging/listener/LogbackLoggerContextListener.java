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

package net.dreamlu.mica.logging.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.logging.appender.ILoggingAppender;

import java.util.List;

/**
 * Logback configuration is achieved by configuration file and API.
 * When configuration file change is detected, the configuration is reset.
 * This listener ensures that the programmatic configuration is also re-applied after reset.
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
	private final List<ILoggingAppender> appenderList;

	@Override
	public boolean isResetResistant() {
		return true;
	}

	@Override
	public void onStart(LoggerContext context) {
		for (ILoggingAppender appender : appenderList) {
			appender.start(context);
		}
	}

	@Override
	public void onReset(LoggerContext context) {
		for (ILoggingAppender appender : appenderList) {
			appender.reset(context);
		}
	}

	@Override
	public void onStop(LoggerContext context) {
		// Nothing to do.
	}

	@Override
	public void onLevelChange(Logger logger, Level level) {
		// Nothing to do.
	}
}
