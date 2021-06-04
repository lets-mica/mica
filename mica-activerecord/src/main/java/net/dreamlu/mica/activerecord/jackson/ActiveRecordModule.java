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

package net.dreamlu.mica.activerecord.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jfinal.plugin.activerecord.Record;
import net.dreamlu.mica.auto.annotation.AutoService;

/**
 * jFinal ActiveRecord jackson Module
 *
 * <p>
 *     说明：
 *     1. Model 通过 get set 方法序列化和反序列化
 *     2. Record 使用自定义的序列化方式解决
 * </p>
 *
 * @author L.cm
 */
@AutoService(Module.class)
public class ActiveRecordModule extends SimpleModule {

	public ActiveRecordModule() {
		super(ActiveRecordModule.class.getName());
		this.addSerializer(Record.class, RecordSerializer.INSTANCE);
		this.addDeserializer(Record.class, RecordDeserializer.INSTANCE);
	}

}
