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

package net.dreamlu.mica.activerecord.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dreamlu.mica.activerecord.dialect.DialectType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jfinal activerecord config
 *
 * @author dream.lu
 */
@Getter
@Setter
@ConfigurationProperties("mica.activerecord")
public class MicaActiveRecordProperties {

	/**
	 * 数据库方言
	 */
	private DialectType dialect = DialectType.Mysql;
	/**
	 * 模型的包路径
	 */
	private String modelPackage;
	/**
	 * 自定表扫描
	 */
	private boolean autoTableScan = true;
	/**
	 * sql 模板前缀
	 */
	private String baseTemplatePath;
	/**
	 * sql 模板，支持多个
	 */
	private String[] sqlTemplates;
	/**
	 * 事务级别，默认：不可重复读
	 */
	private TransactionLevel transactionLevel = TransactionLevel.TRANSACTION_READ_COMMITTED;

	@Getter
	@RequiredArgsConstructor
	public enum TransactionLevel {
		/**
		 * 事务级别
		 */
		TRANSACTION_NONE(0),
		/**
		 * 读未提交
		 */
		TRANSACTION_READ_UNCOMMITTED(1),
		/**
		 * 读提交
		 */
		TRANSACTION_READ_COMMITTED(2),
		/**
		 * 重复读
		 */
		TRANSACTION_REPEATABLE_READ(4),
		/**
		 * 串行化
		 */
		TRANSACTION_SERIALIZABLE(8);
		private final int level;
	}

}
