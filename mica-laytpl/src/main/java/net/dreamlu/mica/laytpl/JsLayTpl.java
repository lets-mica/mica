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

package net.dreamlu.mica.laytpl;

/**
 * laytpl js 源码改版
 *
 * <p>
 *     之前朋友反馈直接读取js文件会出现各种异常问题，故采用 java 字符串源码
 *
 *     震惊，java8 Nashorn和laytpl居然能擦出这样火花！：https://my.oschina.net/qq596392912/blog/872813
 * </p>
 *
 * @author L.cm
 */
public class JsLayTpl {

	public static final String LAY_TPL_JS =
		"var window = {};\n" +
		"\n" +
		"var config = {\n" +
		"  open: _config.open,\n" +
		"  close: _config.close\n" +
		"};\n" +
		"\n" +
		"var tool = {\n" +
		"    exp: function (str) {\n" +
		"        return new RegExp(str, 'g');\n" +
		"    },\n" +
		"    //匹配满足规则内容\n" +
		"    query: function (type, _, __) {\n" +
		"        var types = [\n" +
		"            '#([\\\\s\\\\S])+?',   //js语句\n" +
		"            '([^{#}])*?' //普通字段\n" +
		"        ][type || 0];\n" +
		"        return exp((_ || '') + config.open + types + config.close + (__ || ''));\n" +
		"    },\n" +
		"    escape: function (html) {\n" +
		"        return String(html || '').replace(/&(?!#?[a-zA-Z0-9]+;)/g, '&amp;')\n" +
		"            .replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/'/g, '&#39;').replace(/\"/g, '&quot;');\n" +
		"    },\n" +
		"    error: function (e, tplog) {\n" +
		"        var error = 'Laytpl Error：';\n" +
		"        typeof console === 'object' && console.error(error + e + '\\n' + (tplog || ''));\n" +
		"        return error + e;\n" +
		"    }\n" +
		"};\n" +
		"\n" +
		"var exp = tool.exp, Tpl = function (tpl) {\n" +
		"    this.tpl = tpl;\n" +
		"};\n" +
		"\n" +
		"Tpl.pt = Tpl.prototype;\n" +
		"\n" +
		"window.errors = 0;\n" +
		"\n" +
		"//编译模版\n" +
		"Tpl.pt.parse = function (tpl, data) {\n" +
		"    var that = this, tplog = tpl;\n" +
		"    var jss = exp('^' + config.open + '#', ''), jsse = exp(config.close + '$', '');\n" +
		"\n" +
		"    tpl = tpl.replace(/\\s+|\\r|\\t|\\n/g, ' ')\n" +
		"        .replace(exp(config.open + '#'), config.open + '# ')\n" +
		"        .replace(exp(config.close + '}'), '} ' + config.close).replace(/\\\\/g, '\\\\\\\\')\n" +
		"        //不匹配指定区域的内容\n" +
		"        .replace(exp(config.open + '!(.+?)!' + config.close), function (str) {\n" +
		"            str = str.replace(exp('^' + config.open + '!'), '')\n" +
		"                .replace(exp('!' + config.close), '')\n" +
		"                .replace(exp(config.open + '|' + config.close), function (tag) {\n" +
		"                    return tag.replace(/(.)/g, '\\\\$1')\n" +
		"                });\n" +
		"            return str\n" +
		"        })\n" +
		"        //匹配JS规则内容\n" +
		"        .replace(/(?=\"|')/g, '\\\\').replace(tool.query(), function (str) {\n" +
		"            str = str.replace(jss, '').replace(jsse, '');\n" +
		"            return '\";' + str.replace(/\\\\/g, '') + ';view+=\"';\n" +
		"        })\n" +
		"        //匹配普通字段\n" +
		"        .replace(tool.query(1), function (str) {\n" +
		"            var start = '\"+(';\n" +
		"            if (str.replace(/\\s/g, '') === config.open + config.close) {\n" +
		"                return '';\n" +
		"            }\n" +
		"            str = str.replace(exp(config.open + '|' + config.close), '');\n" +
		"            if (/^=/.test(str)) {\n" +
		"                str = str.replace(/^=/, '');\n" +
		"                start = '\"+_escape_(';\n" +
		"            }\n" +
		"            return start + str.replace(/\\\\/g, '') + ')+\"';\n" +
		"        });\n" +
		"\n" +
		"    tpl = '\"use strict\";var view = \"' + tpl + '\";return view;';\n" +
		"\n" +
		"    try {\n" +
		"        that.cache = tpl = new Function('d, _escape_', tpl);\n" +
		"        return tpl(data, tool.escape);\n" +
		"    } catch (e) {\n" +
		"        delete that.cache;\n" +
		"        return tool.error(e, tplog);\n" +
		"    }\n" +
		"};\n" +
		"\n" +
		"Tpl.pt.render = function (data, callback) {\n" +
		"    var that = this, tpl;\n" +
		"    if (!data) return tool.error('no data');\n" +
		"    tpl = that.cache ? that.cache(data, tool.escape) : that.parse(that.tpl, data);\n" +
		"    if (!callback) return tpl;\n" +
		"    callback(tpl);\n" +
		"};\n" +
		"\n" +
		"var laytpl = function (tpl) {\n" +
		"    if (typeof tpl !== 'string') return tool.error('Template not found');\n" +
		"    return new Tpl(tpl);\n" +
		"};\n" +
		"\n" +
		"laytpl.config = function (options) {\n" +
		"    options = options || {};\n" +
		"    for (var i in options) {\n" +
		"        config[i] = options[i];\n" +
		"    }\n" +
		"};\n" +
		"\n" +
		"laytpl.v = '1.2.0';";
}
