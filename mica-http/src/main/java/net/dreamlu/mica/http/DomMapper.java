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

import net.dreamlu.mica.core.utils.Exceptions;
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
public class DomMapper {

	/**
	 * Returns body to jsoup Document.
	 *
	 * @return Document
	 */
	public static Document asDocument(ResponseSpec response) {
		return readDocument(response.asString());
	}

	/**
	 * 将流读取为 jsoup Document
	 *
	 * @param inputStream InputStream
	 * @return Document
	 */
	public static Document readDocument(InputStream inputStream) {
		try {
			return DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将 html 字符串读取为 jsoup Document
	 *
	 * @param html String
	 * @return Document
	 */
	public static Document readDocument(String html) {
		return Parser.parse(html, "");
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param inputStream InputStream
	 * @param clazz       bean Class
	 * @param <T>         泛型
	 * @return 对象
	 */
	public static <T> T readValue(InputStream inputStream, final Class<T> clazz) {
		return readValue(readDocument(inputStream), clazz);
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
		return readValue(readDocument(html), clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param doc   xml element
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
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
		return readList(readDocument(inputStream), clazz);
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
		return readList(readDocument(html), clazz);
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
