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

package net.dreamlu.mica.core.convert;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;

/**
 * 类型 转换 服务，添加了 IEnum 转换
 *
 * @author L.cm
 */
public class MicaConversionService extends DefaultFormattingConversionService {
	@Nullable
	private static volatile MicaConversionService SHARED_INSTANCE;

	public MicaConversionService() {
		super();
		super.addConverter(new EnumToStringConverter());
		super.addConverter(new StringToEnumConverter());
	}

	/**
	 * Return a shared default application {@code ConversionService} instance, lazily
	 * building it once needed.
	 * <p>
	 * Note: This method actually returns an {@link MicaConversionService}
	 * instance. However, the {@code ConversionService} signature has been preserved for
	 * binary compatibility.
	 *
	 * @return the shared {@code MicaConversionService} instance (never{@code null})
	 */
	public static GenericConversionService getInstance() {
		MicaConversionService sharedInstance = MicaConversionService.SHARED_INSTANCE;
		if (sharedInstance == null) {
			synchronized (MicaConversionService.class) {
				sharedInstance = MicaConversionService.SHARED_INSTANCE;
				if (sharedInstance == null) {
					sharedInstance = new MicaConversionService();
					MicaConversionService.SHARED_INSTANCE = sharedInstance;
				}
			}
		}
		return sharedInstance;
	}

}
