package net.dreamlu.mica.mybatis.plugins;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

/**
 * 用于输出每条 SQL 语句及其执行时间
 *
 * @author hubin nieqiurong TaoYu
 * @since 2016-07-07
 */
@Slf4j
@Intercepts({
	@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
	@Signature(type = StatementHandler.class, method = "update", args = Statement.class),
	@Signature(type = StatementHandler.class, method = "batch", args = Statement.class)
})
public class SqlLogInterceptor implements Interceptor {
	private static final String DRUID_POOLED_PREPARED_STATEMENT = "com.alibaba.druid.pool.DruidPooledPreparedStatement";
	private static final String T4C_PREPARED_STATEMENT = "oracle.jdbc.driver.T4CPreparedStatement";
	private static final String ORACLE_PREPARED_STATEMENT_WRAPPER = "oracle.jdbc.driver.OraclePreparedStatementWrapper";

	private Method oracleGetOriginalSqlMethod;
	private Method druidGetSqlMethod;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Statement statement;
		Object firstArg = invocation.getArgs()[0];
		if (Proxy.isProxyClass(firstArg.getClass())) {
			statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
		} else {
			statement = (Statement) firstArg;
		}
		MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);
		try {
			statement = (Statement) stmtMetaObj.getValue("stmt.statement");
		} catch (Exception e) {
			// do nothing
		}
		if (stmtMetaObj.hasGetter("delegate")) {
			//Hikari
			try {
				statement = (Statement) stmtMetaObj.getValue("delegate");
			} catch (Exception ignored) {

			}
		}

		String originalSql = null;
		String stmtClassName = statement.getClass().getName();
		if (DRUID_POOLED_PREPARED_STATEMENT.equals(stmtClassName)) {
			try {
				if (druidGetSqlMethod == null) {
					Class<?> clazz = Class.forName(DRUID_POOLED_PREPARED_STATEMENT);
					druidGetSqlMethod = clazz.getMethod("getSql");
				}
				Object stmtSql = druidGetSqlMethod.invoke(statement);
				if (stmtSql instanceof String) {
					originalSql = (String) stmtSql;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (T4C_PREPARED_STATEMENT.equals(stmtClassName)
			|| ORACLE_PREPARED_STATEMENT_WRAPPER.equals(stmtClassName)) {
			try {
				if (oracleGetOriginalSqlMethod != null) {
					Object stmtSql = oracleGetOriginalSqlMethod.invoke(statement);
					if (stmtSql instanceof String) {
						originalSql = (String) stmtSql;
					}
				} else {
					Class<?> clazz = Class.forName(stmtClassName);
					oracleGetOriginalSqlMethod = getMethodRegular(clazz, "getOriginalSql");
					if (oracleGetOriginalSqlMethod != null) {
						//OraclePreparedStatementWrapper is not a public class, need set this.
						oracleGetOriginalSqlMethod.setAccessible(true);
						if (null != oracleGetOriginalSqlMethod) {
							Object stmtSql = oracleGetOriginalSqlMethod.invoke(statement);
							if (stmtSql instanceof String) {
								originalSql = (String) stmtSql;
							}
						}
					}
				}
			} catch (Exception e) {
				//ignore
			}
		}
		if (originalSql == null) {
			originalSql = statement.toString();
		}
		originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);
		int index = indexOfSqlStart(originalSql);
		if (index > 0) {
			originalSql = originalSql.substring(index);
		}

		// 计算执行 SQL 耗时
		long start = SystemClock.now();
		Object result = invocation.proceed();
		long timing = SystemClock.now() - start;

		// SQL 打印执行结果
		Object target = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(target);
		MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

		// 打印 sql
		log.info(" Time：{} ms - ID：{}\nExecute SQL：{}", timing, ms.getId(), originalSql);
		return result;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	/**
	 * 获取此方法名的具体 Method
	 *
	 * @param clazz      class 对象
	 * @param methodName 方法名
	 * @return 方法
	 */
	private Method getMethodRegular(Class<?> clazz, String methodName) {
		if (Object.class.equals(clazz)) {
			return null;
		}
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return getMethodRegular(clazz.getSuperclass(), methodName);
	}

	/**
	 * 获取sql语句开头部分
	 *
	 * @param sql ignore
	 * @return ignore
	 */
	private int indexOfSqlStart(String sql) {
		String upperCaseSql = sql.toUpperCase();
		Set<Integer> set = new HashSet<>();
		set.add(upperCaseSql.indexOf("SELECT "));
		set.add(upperCaseSql.indexOf("UPDATE "));
		set.add(upperCaseSql.indexOf("INSERT "));
		set.add(upperCaseSql.indexOf("DELETE "));
		set.remove(-1);
		if (CollectionUtils.isEmpty(set)) {
			return -1;
		}
		List<Integer> list = new ArrayList<>(set);
		list.sort(Comparator.naturalOrder());
		return list.get(0);
	}

}
