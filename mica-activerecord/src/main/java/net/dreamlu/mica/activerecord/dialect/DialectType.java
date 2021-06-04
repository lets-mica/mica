package net.dreamlu.mica.activerecord.dialect;

import com.jfinal.plugin.activerecord.dialect.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据库方言
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum DialectType {

	/**
	 * 方言
	 */
	Mysql,
	Oracle,
	Sqlite3,
	Postgre,
	SqlServer,
	Ansi,
	Druid;

	public Dialect getDialect() {
		switch (this) {
			case Mysql: return new MysqlDialect();
			case Oracle: return new OracleDialect();
			case Sqlite3: return new Sqlite3Dialect();
			case Postgre: return new PostgreSqlDialect();
			case SqlServer: return new SqlServerDialect();
			case Ansi: return new AnsiSqlDialect();
			default: return new DruidSqlDialect();
		}
	}

}
