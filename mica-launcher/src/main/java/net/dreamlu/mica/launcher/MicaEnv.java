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

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目启动器，预制的环境变量
 *
 * @author L.cm
 */
@Getter
@AllArgsConstructor
public enum MicaEnv {
	/**
	 * dev（开发），默认。
	 */
	DEV( "dev", "debug", MicaLogLevel.BODY),
	/**
	 * test（测试）
	 */
	TEST("test", "info", MicaLogLevel.BODY),
	/**
	 * ontest（线上测试）
	 */
	ON_TEST( "ontest", "info", MicaLogLevel.HEADERS),
	/**
	 * prod（正式）
	 */
	PROD("prod", "warn", MicaLogLevel.NONE);

	/**
	 * 等级
	 */
	private String name;
	/**
	 * log文件的级别
	 */
	private String logFileLevel;
	/**
	 * 请求日志级别
	 */
	private MicaLogLevel reqLogLevel;

	/**
	 * 获取环境列表
	 *
	 * @return 环境列表
	 */
	public static List<String> getEnvList() {
		return Arrays.stream(MicaEnv.values())
			.map(MicaEnv::getName)
			.collect(Collectors.toList());
	}

	/**
	 * 环境转换
	 *
	 * @return 环境
	 */
	public static MicaEnv of(String env) {
		MicaEnv[] values = MicaEnv.values();
		for (MicaEnv micaEnv : values) {
			if (micaEnv.name.equals(env)) {
				return micaEnv;
			}
		}
		return MicaEnv.DEV;
	}
}
