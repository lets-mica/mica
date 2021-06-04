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

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import javax.sql.DataSource;

/**
 * 数据字典生成
 *
 * @author L.cm
 */
public class DataDictionaryGenerator extends com.jfinal.plugin.activerecord.generator.DataDictionaryGenerator {

	public DataDictionaryGenerator(DataSource dataSource, String dataDictionaryOutputDir) {
		super(dataSource, dataDictionaryOutputDir);
		setDataDictionaryFileName("DataDictionary.md");
	}

	/**
	 * 重写掉生成换行
	 */
	@Override
	protected String genSeparateLine(TableMeta tm) {
		return "";
	}

	@Override
	protected void generateTable(TableMeta tableMeta, StringBuilder ret) {
		ret.append("Table: ").append(tableMeta.name);
		if (StrKit.notBlank(tableMeta.remarks)) {
			ret.append("（").append(clearBlank(tableMeta.remarks)).append("）");
		}
		ret.append("\n");

		String separateLine = genSeparateLine(tableMeta);
		ret.append(separateLine);
		genTableHead(tableMeta, ret);
		ret.append(separateLine);
		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
			genColumn(tableMeta, columnMeta, ret);
		}
		ret.append(separateLine);
		ret.append("\n");
	}

	/**
	 * 表头
	 */
	@Override
	protected void genTableHead(TableMeta tm, StringBuilder ret) {
		ret.append('\n').append('|');
		genCell(tm.colNameMaxLen, " ", "Field", " ", "|",  ret);
		genCell(tm.colTypeMaxLen, " ", "Type", " ", "|", ret);
		genCell("Null".length(), " ", "Null",  " ","|", ret);
		genCell("Key".length(), " ", "Key",  " ","|", ret);
		genCell(tm.colDefaultValueMaxLen, " ", "Default", " ", "|", ret);
		genCell("Remarks".length(), " ", "Remarks", " ", "|", ret);
		ret.append('\n').append('|');
		genCell(tm.colNameMaxLen, " ", "-", "-", "|",  ret);
		ret.replace(ret.length() - 2, ret.length() - 1, " ");
		genCell(tm.colTypeMaxLen, " ", "-", "-", "|", ret);
		ret.replace(ret.length() - 2, ret.length() - 1, " ");
		genCell("Null".length(), " ", "-",  "-","|", ret);
		ret.replace(ret.length() - 2, ret.length() - 1, " ");
		genCell("Key".length(), " ", "-",  "-","|", ret);
		ret.replace(ret.length() - 2, ret.length() - 1, " ");
		genCell(tm.colDefaultValueMaxLen, " ", "-", "-", "|", ret);
		ret.replace(ret.length() - 2, ret.length() - 1, " ");
		genCell("Remarks".length(), " ", "-", "-", "|", ret);
		ret.replace(ret.length() - 2, ret.length() - 1, " ");
		ret.append('\n');
	}

	/**
	 * 列
	 */
	@Override
	protected void genColumn(TableMeta tableMeta, ColumnMeta columnMeta, StringBuilder ret) {
		ret.append('|');
		genCell(tableMeta.colNameMaxLen, " ", columnMeta.name, " ", "|",  ret);
		genCell(tableMeta.colTypeMaxLen, " ", columnMeta.type, " ", "|", ret);
		genCell("Null".length(), " ", columnMeta.isNullable, " ", "|", ret);
		genCell("Key".length(), " ", columnMeta.isPrimaryKey, " ", "|", ret);
		genCell(tableMeta.colDefaultValueMaxLen, " ", columnMeta.defaultValue, " ", "|", ret);
		genCell("Remarks".length(), " ", clearBlank(columnMeta.remarks), " ", "|", ret);
		ret.append("\n");
	}

	/**
	 * 清除注释中的空白符
	 */
	private String clearBlank(String str) {
		return str.replaceAll("[ |\t|\n]+", "");
	}
}
