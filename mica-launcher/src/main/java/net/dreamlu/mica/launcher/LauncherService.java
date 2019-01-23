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

package net.dreamlu.mica.launcher;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * launcher 扩展 用于一些组件发现
 *
 * @author L.cm
 */
public interface LauncherService {

	/**
	 * 启动时 处理 SpringApplicationBuilder
	 * @param builder SpringApplicationBuilder
	 * @param appName 服务名
	 * @param profile 环境变量
	 * @param isLocalDev 是否本地开发
	 */
	void launcher(SpringApplicationBuilder builder, String appName, String profile, boolean isLocalDev);
}
