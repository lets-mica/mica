/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.http;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.cglib.proxy.Enhancer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫 xml 转 bean 基于 jsoup
 *
 * @author L.cm
 */
@SuppressWarnings("unchecked")
public class DomMapper {

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param inputStream InputStream
	 * @param clazz       bean Class
	 * @param <T>         泛型
	 * @return 对象
	 */
	public static <T> T readValue(InputStream inputStream, final Class<T> clazz) {
		try {
			Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
			return readValue(document, clazz);
		} catch (IOException e) {
			throw new MicaHttpException(e);
		}
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param html  html String
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象
	 */
	public static <T> T readValue(String html, final Class<T> clazz) {
		Document document = Parser.parse(html, "");
		return readValue(document, clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param doc   xml element
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象
	 */
	public static <T> T readValue(final Element doc, final Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setUseCache(true);
		enhancer.setCallback(new CssQueryMethodInterceptor(clazz, doc));
		return (T) enhancer.create();
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param <T>         泛型
	 * @param inputStream InputStream
	 * @param clazz       bean Class
	 * @return 对象
	 */
	public static <T> List<T> readList(InputStream inputStream, final Class<T> clazz) {
		try {
			Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
			return readList(document, clazz);
		} catch (IOException e) {
			throw new MicaHttpException(e);
		}
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param <T>   泛型
	 * @param html  html String
	 * @param clazz bean Class
	 * @return 对象
	 */
	public static <T> List<T> readList(String html, final Class<T> clazz) {
		Document document = Parser.parse(html, "");
		return readList(document, clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param doc   xml element
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象列表
	 */
	public static <T> List<T> readList(Element doc, Class<T> clazz) {
		CssQuery annotation = clazz.getAnnotation(CssQuery.class);
		if (annotation == null) {
			throw new IllegalArgumentException("DomMapper readList " + clazz + " mast has annotation @CssQuery.");
		}
		String cssQueryValue = annotation.value();
		Elements elements = doc.select(cssQueryValue);
		List<T> valueList = new ArrayList<>();
		for (Element element : elements) {
			valueList.add(readValue(element, clazz));
		}
		return valueList;
	}

}
