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

package net.dreamlu.mica.activerecord.dialect;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.util.JdbcUtils;
import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.NumberUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 利用 druid 优化分页语句，提升性能
 *
 * @author L.cm
 */
public class DruidSqlDialect extends AnsiSqlDialect {

	@Override
	public boolean isTakeOverDbPaginate() {
		return true;
	}

	@Override
	public Page<Record> takeOverDbPaginate(Connection conn, int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, StringBuilder findSqlBuilder, Object... paras) throws SQLException {
		// 数据库类型
		DbType dbType = JdbcUtils.getDbTypeRaw(conn.getMetaData().getURL(), null);
		// sql
		String findSql = findSqlBuilder.toString();
		// 解析 count sql，先转成带 #{0} 的 sql
		String paramSql = toParamSql(findSql);
		String countSql = PagerUtils.count(paramSql, dbType);
		// 解析 count 变量，druid 优化后变量会变
		Object[] countSqlParams = getCountSqlParams(countSql, paras);
		// 分页 count 结果
		List<Object> result = CPI.query(conn, getCountPreSql(countSql), countSqlParams);
		int size = result.size();
		long totalRow = (size > 0) ? ((Number) result.get(0)).longValue() : 0;
		if (totalRow == 0) {
			return new Page<>(new ArrayList<>(0), pageNumber, pageSize, 0, 0);
		}
		int totalPage = (int) (totalRow / pageSize);
		if (totalRow % pageSize != 0) {
			totalPage++;
		}
		if (pageNumber > totalPage) {
			return new Page<>(new ArrayList<>(0), pageNumber, pageSize, totalPage, (int) totalRow);
		}
		// --------
		String sql = PagerUtils.limit(findSql, dbType, pageNumber, pageSize);
		List<Record> list = CPI.find(conn, sql, paras);
		return new Page<>(list, pageNumber, pageSize, totalPage, (int) totalRow);
	}

	@Override
	public boolean isTakeOverModelPaginate() {
		return true;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Page<? extends Model> takeOverModelPaginate(Connection conn, Class<? extends Model> modelClass, int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, StringBuilder findSqlBuilder, Object... paras) throws Exception {
		// 数据库类型
		DbType dbType = JdbcUtils.getDbTypeRaw(conn.getMetaData().getURL(), null);
		// sql
		String findSql = findSqlBuilder.toString();
		// 解析 count sql，先转成带 #{0} 的 sql
		String paramSql = toParamSql(findSql);
		String countSql = PagerUtils.count(paramSql, dbType);
		// 解析 count 变量，druid 优化后变量会变
		Object[] countSqlParams = getCountSqlParams(countSql, paras);
		// 分页 count 结果
		List<Object> result = CPI.query(conn, getCountPreSql(countSql), countSqlParams);
		int size = result.size();
		long totalRow = (size > 0) ? ((Number) result.get(0)).longValue() : 0;
		if (totalRow == 0) {
			return new Page<>(new ArrayList<>(0), pageNumber, pageSize, 0, 0);
		}
		int totalPage = (int) (totalRow / pageSize);
		if (totalRow % pageSize != 0) {
			totalPage++;
		}
		if (pageNumber > totalPage) {
			return new Page<>(new ArrayList<>(0), pageNumber, pageSize, totalPage, (int) totalRow);
		}
		// --------
		String sql = PagerUtils.limit(findSql, dbType, pageNumber, pageSize);
		List<Model> list = find(conn, modelClass, sql, paras);
		return new Page<>(list, pageNumber, pageSize, totalPage, (int) totalRow);
	}

	/**
	 * Find model.
	 * <p>
	 * 警告：传入的 Connection 参数需要由传入者在 try finally 块中自行
	 * 关闭掉，否则将出现 Connection 资源不能及时回收的问题
	 */
	protected <M> List<M> find(Connection conn, Class<? extends Model> modelClass, String sql, Object... paras) throws Exception {
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			fillStatement(pst, paras);
			ResultSet rs = pst.executeQuery();
			List<M> result = buildModelList(rs, modelClass);
			JdbcUtils.close(rs);
			return result;
		}
	}

	public static String toParamSql(String sql) {
		StringBuilder sb = new StringBuilder((int) (sql.length() * 1.1));
		int cursor = 0;
		for (int start, index = 0; (start = sql.indexOf(CharPool.QUESTION_MARK, cursor)) != -1; ) {
			sb.append(sql, cursor, start);
			sb.append("#{").append(index).append('}');
			cursor = start + 1;
			index++;
		}
		sb.append(sql.substring(cursor));
		return sb.toString();
	}

	public static Object[] getCountSqlParams(String sql, Object[] paras) {
		List<Object> params = new ArrayList<>();
		int cursor = 0;
		int index = 0;
		int parasLength = paras.length;
		for (int start, end; (start = sql.indexOf("#{", cursor)) != -1 && (end = sql.indexOf(CharPool.RIGHT_BRACE, start)) != -1 && index < parasLength; ) {
			String paramIdx = sql.substring(start + 2, end);
			params.add(paras[NumberUtil.toInt(paramIdx)]);
			cursor = end + 1;
			index++;
		}
		return params.toArray();
	}

	/**
	 * 获取 count sql
	 * @param paramSql 参数 sql
	 * @return sql
	 */
	public static String getCountPreSql(String paramSql) {
		return paramSql.replaceAll("#\\{\\w}", "?");
	}

}
