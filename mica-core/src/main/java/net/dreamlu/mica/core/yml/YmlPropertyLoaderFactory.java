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

package net.dreamlu.mica.core.yml;

import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * YmlPropertyLoaderFactory
 *
 * @author lcm
 */
public class YmlPropertyLoaderFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource encodedResource) throws IOException {
		if (encodedResource == null) {
			return emptyPropertySource(name);
		}
		Resource resource = encodedResource.getResource();
		String fileName = resource.getFilename();
		List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(fileName, resource);
		return sources.isEmpty() ? emptyPropertySource(fileName) : sources.get(0);
	}

	private static PropertySource<?> emptyPropertySource(@Nullable String name) {
		return new OriginTrackedMapPropertySource(name, Collections.emptyMap());
	}
}
