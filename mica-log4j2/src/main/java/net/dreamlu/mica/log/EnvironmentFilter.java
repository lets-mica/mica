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

package net.dreamlu.mica.log;

import net.dreamlu.mica.config.SpringUtils;
import net.dreamlu.mica.core.utils.ObjectUtil;
import net.dreamlu.mica.launcher.MicaLogLevel;
import net.dreamlu.mica.props.MicaProperties;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.core.env.Environment;

/**
 * 基于 Environment 的 log4j 日志过滤器.
 *
 * @author L.cm
 */
@Plugin(name = "EnvironmentFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
@PerformanceSensitive("allocation")
public class EnvironmentFilter extends AbstractFilter {

	public EnvironmentFilter() {
		super(Result.ACCEPT, Result.DENY);
	}

	protected Result filterByEnvironment() {
		MicaProperties micaProperties = SpringUtils.getBean(MicaProperties.class);
		if (micaProperties == null) {
			return Result.ACCEPT;
		}
		Boolean isLocal = micaProperties.getIsLocal();
		if (ObjectUtil.isTrue(isLocal)) {
			return Result.ACCEPT;
		}
		Environment environment = micaProperties.getEnvironment();
		Boolean isConsoleEnabled = environment.getProperty(MicaLogLevel.CONSOLE_LOG_ENABLED_PROP, Boolean.class);
		if (ObjectUtil.isTrue(isConsoleEnabled)) {
			return Result.ACCEPT;
		}
		return Result.DENY;
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg,
						 final Object... params) {
		return filterByEnvironment();
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg,
						 final Throwable t) {
		return filterByEnvironment();
	}

	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg,
						 final Throwable t) {
		return filterByEnvironment();
	}

	@Override
	public Result filter(final LogEvent event) {
		return filterByEnvironment();
	}

	@PluginFactory
	public static EnvironmentFilter createFilter() {
		return new EnvironmentFilter();
	}
}
