/*
 * Copyright (c) 2016-2020, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.xss.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xss clean
 *
 * <p>
 * 参考自 jpress：https://gitee.com/fuhai/jpress
 * </p>
 *
 * @author L.cm
 * @author michael
 */
public class XssUtil {
	/**
	 * html 实体，只匹配带分号的命名实体与数字实体
	 */
	private static final Pattern ENTITY_PATTERN = Pattern.compile(
		"&(#[0-9]+|#[xX][0-9a-fA-F]+|[a-zA-Z][a-zA-Z0-9]*);");

	public static final HtmlSafeList WHITE_LIST = HtmlSafeList.INSTANCE;

	/**
	 * trim 字符串
	 *
	 * @param text text
	 * @return 清理后的 text
	 */
	public static String trim(String text, boolean trim) {
		return trim ? text.trim() : text;
	}

	/**
	 * xss 清理
	 *
	 * @param html html
	 * @return 清理后的 html
	 */
	public static String clean(String html) {
		if (StringUtils.hasText(html)) {
			return Jsoup.clean(html, WHITE_LIST);
		}
		return html;
	}

	/**
	 * 反转义 html 实体，但不会还原出能生成标签或属性的字符
	 *
	 * <p>
	 * 全量反转义（{@link Entities#unescape}）会把实体编码的标签还原成真实标签，
	 * 也会把属性值中的双引号实体还原成双引号导致属性逃逸。
	 * 这里逐个实体反转义，保证只解一层（jsoup 的反转义会重扫，一层就能解出多层），
	 * 且解码结果包含 <、>、" 时保持转义，因此不会还原出可执行的标签与事件属性。
	 *
	 * @param html html
	 * @return 反转义后的 html
	 */
	@Nullable
	public static String unescapeEntities(@Nullable String html) {
		if (html == null || html.indexOf('&') < 0) {
			return html;
		}
		Matcher matcher = ENTITY_PATTERN.matcher(html);
		StringBuilder sb = new StringBuilder(html.length());
		int last = 0;
		while (matcher.find()) {
			// 逐个实体反转义，strict 表示必须带分号才反转义
			String decoded = Parser.unescapeEntities(matcher.group(), true);
			if (!isSafeEntityValue(decoded)) {
				// 会还原成标签或属性的实体，保持转义
				continue;
			}
			sb.append(html, last, matcher.start()).append(decoded);
			last = matcher.end();
		}
		if (last == 0) {
			return html;
		}
		return sb.append(html, last, html.length()).toString();
	}

	/**
	 * 实体的解码结果中不能包含能生成标签或属性的字符
	 *
	 * @param value 实体的解码结果
	 * @return 是否安全
	 */
	private static boolean isSafeEntityValue(String value) {
		return value.indexOf('<') < 0 && value.indexOf('>') < 0 && value.indexOf('"') < 0;
	}

	/**
	 * 做自己的白名单，允许base64的图片通过等
	 *
	 * @author michael
	 */
	public static class HtmlSafeList extends org.jsoup.safety.Safelist {
		public static final HtmlSafeList INSTANCE = new HtmlSafeList();

		/**
		 * 允许的 url 协议
		 */
		private static final Set<String> SAFE_PROTOCOLS = Set.of("http", "https", "ftp", "mailto", "tel");

		/**
		 * 需要校验协议的属性
		 */
		private static final Set<String> URL_ATTRIBUTES = Set.of("href", "src", "cite", "codebase", "poster",
			"background", "action", "formaction", "data", "longdesc", "manifest", "profile", "usemap", "ping");

		/**
		 * url 协议格式，如 http、https、javascript
		 */
		private static final Pattern PROTOCOL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.\\-]*$");

		/**
		 * css 注释
		 */
		private static final Pattern CSS_COMMENT_PATTERN = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);

		/**
		 * css 转义，如 \6a 相当于 j
		 */
		private static final Pattern CSS_ESCAPE_PATTERN = Pattern.compile("\\\\([0-9a-fA-F]{1,6})[ \t\r\n\f]?");

		public HtmlSafeList() {
			addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "span", "embed", "object", "dl", "dt",
				"em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small",
				"strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul");

			addAttributes("a", "href", "title", "target");
			addAttributes("blockquote", "cite");
			addAttributes("col", "span");
			addAttributes("colgroup", "span");
			addAttributes("img", "align", "alt", "src", "title");
			addAttributes("ol", "start");
			addAttributes("q", "cite");
			addAttributes("table", "summary");
			addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width");
			addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width");
			addAttributes("video", "src", "autoplay", "controls", "loop", "muted", "poster", "preload");
			addAttributes("object", "width", "height", "classid", "codebase");
			addAttributes("param", "name", "value");
			addAttributes("embed", "src", "quality", "width", "height", "allowFullScreen", "allowScriptAccess", "flashvars", "name", "type", "pluginspage");

			addAttributes(":all", "class", "style", "height", "width", "type", "id", "name");

			addProtocols("blockquote", "cite", "http", "https");
			addProtocols("cite", "cite", "http", "https");
			addProtocols("q", "cite", "http", "https");

			//如果添加以下的协议，那么href 必须是http、 https 等开头，相对路径则被过滤掉了
			//addProtocols("a", "href", "ftp", "http", "https", "mailto", "tel");

			//如果添加以下的协议，那么src必须是http 或者 https 开头，相对路径则被过滤掉了，
			//所以必须注释掉，允许相对路径的图片资源
			//addProtocols("img", "src", "http", "https");
		}

		@Override
		public boolean isSafeAttribute(String tagName, Element el, Attribute attr) {
			String attrKey = attr.getKey();
			// url 属性需要校验协议，防止 javascript、vbscript、data 等协议绕过
			if (URL_ATTRIBUTES.contains(attrKey.toLowerCase(Locale.ROOT))) {
				String value = attr.getValue();
				//允许 base64 的图片内容
				if ("img".equals(tagName) && "src".equalsIgnoreCase(attrKey) && isSafeImageDataUri(value)) {
					return true;
				}
				if (!isSafeUrl(value)) {
					return false;
				}
			}
			// style 里可能藏着脚本协议与旧式表达式
			if ("style".equalsIgnoreCase(attrKey) && !isSafeStyle(attr.getValue())) {
				return false;
			}
			return super.isSafeAttribute(tagName, el, attr);
		}

		/**
		 * 校验 style 是否安全，css 中不允许出现脚本协议与旧式表达式
		 *
		 * <p>
		 * 现代浏览器已不会执行 css 里的 javascript: 与 expression()，这里主要是防止旧内核执行，
		 * 同时避免 style 被用来加载不可信的外链资源。
		 *
		 * @param value style 值
		 * @return 是否安全
		 */
		private static boolean isSafeStyle(String value) {
			if (!StringUtils.hasText(value)) {
				return true;
			}
			String css = normalizeCss(value);
			return !(css.contains("javascript:")
				|| css.contains("vbscript:")
				|| css.contains("expression(")
				|| css.contains("behavior:")
				|| css.contains("-moz-binding")
				|| css.contains("@import"));
		}

		/**
		 * 归一化 css，只用于检测危险构造
		 *
		 * <p>
		 * 去掉注释（避免用注释拆开关键字来绕过）、还原 css 转义（\6a 相当于 j）、
		 * 去掉空白与控制字符（避免 java script: 这种绕过）。
		 *
		 * @param value style 值
		 * @return 归一化后的 css
		 */
		private static String normalizeCss(String value) {
			// 去掉注释
			String css = CSS_COMMENT_PATTERN.matcher(value).replaceAll("");
			// 还原 css 转义
			Matcher matcher = CSS_ESCAPE_PATTERN.matcher(css);
			StringBuilder sb = new StringBuilder(css.length());
			int last = 0;
			while (matcher.find()) {
				sb.append(css, last, matcher.start());
				int codePoint = Integer.parseInt(matcher.group(1), 16);
				if (codePoint > 0 && codePoint <= Character.MAX_CODE_POINT) {
					sb.appendCodePoint(codePoint);
				}
				last = matcher.end();
			}
			sb.append(css, last, css.length());
			// 去掉空白、控制字符与转义符
			StringBuilder result = new StringBuilder(sb.length());
			for (int i = 0; i < sb.length(); i++) {
				char c = sb.charAt(i);
				if (c > ' ' && c != '\\') {
					result.append(Character.toLowerCase(c));
				}
			}
			return result.toString();
		}

		/**
		 * 校验 url 是否安全，只允许 http、https、ftp、mailto、tel 协议，相对地址不受限制
		 *
		 * @param value url
		 * @return 是否安全
		 */
		private static boolean isSafeUrl(String value) {
			if (!StringUtils.hasText(value)) {
				return true;
			}
			// 浏览器解析协议时会忽略空白与控制字符，这里先剔除
			String url = removeControlChar(value);
			int index = url.indexOf(':');
			if (index <= 0) {
				// 相对地址、锚点、协议相对地址
				return true;
			}
			String protocol = url.substring(0, index);
			if (!PROTOCOL_PATTERN.matcher(protocol).matches()) {
				// 不是协议，如 a/b:c
				return true;
			}
			return SAFE_PROTOCOLS.contains(protocol.toLowerCase(Locale.ROOT));
		}

		/**
		 * 剔除 url 中的空白与控制字符（含 BOM）
		 *
		 * @param value url
		 * @return 处理后的 url
		 */
		private static String removeControlChar(String value) {
			StringBuilder sb = new StringBuilder(value.length());
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if (c <= ' ' || c == '\u007F' || c == '\uFEFF') {
					continue;
				}
				sb.append(c);
			}
			return sb.toString();
		}

		/**
		 * 是否为安全的图片 data uri，仅允许图片类型，svg 可能包含脚本，不允许
		 *
		 * @param value data uri
		 * @return 是否安全
		 */
		private static boolean isSafeImageDataUri(String value) {
			if (!StringUtils.hasText(value)) {
				return false;
			}
			String url = removeControlChar(value).toLowerCase(Locale.ROOT);
			if (!url.startsWith("data:")) {
				return false;
			}
			// 兼容历史写法 data:;base64
			if (url.startsWith("data:;base64")) {
				return true;
			}
			int index = url.indexOf(',');
			if (index < 0) {
				return false;
			}
			String meta = url.substring(0, index);
			return meta.startsWith("data:image/") && !meta.startsWith("data:image/svg");
		}
	}

}
