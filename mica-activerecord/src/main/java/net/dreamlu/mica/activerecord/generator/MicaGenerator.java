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

package net.dreamlu.mica.activerecord.generator;

import com.jfinal.plugin.activerecord.generator.MappingKitGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import net.dreamlu.mica.core.utils.StringPool;

import java.util.List;

/**
 * 代码生成
 *
 * @author L.cm
 */
public class MicaGenerator {
	private static final String DEFAULT_PKG = "net.dreamlu";
	public static final EmptyMappingKitGenerator EMPTY_MAPPING = new EmptyMappingKitGenerator();

	public static class EmptyMappingKitGenerator extends MappingKitGenerator {
		private EmptyMappingKitGenerator() {
			super(DEFAULT_PKG, DEFAULT_PKG);
		}

		@Override
		public void generate(List<TableMeta> tableMetas) {

		}
	}

	public static String getModelTemplatePath() {
		return StringPool.SLASH + MicaGenerator.class.getPackage().getName()
			.replace(StringPool.DOT, StringPool.SLASH)
			.concat("/model_template.jf");
	}

}
