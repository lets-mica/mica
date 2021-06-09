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

package net.dreamlu.mica.activerecord.tx;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.NestedTransactionHelpException;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import net.dreamlu.mica.core.utils.Exceptions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * jfinal 事务 aop
 *
 * @author L.cm
 */
@Aspect
public class ActiveRecordTxAspect {

	@Around("@annotation(tx)")
	public Object doAround(ProceedingJoinPoint point, Tx tx) throws Throwable {
		Object retVal = null;
		Config config = getConfigWithTxConfig(point);
		Connection conn = config.getThreadLocalConnection();
		// Nested transaction support
		if (conn != null) {
			try {
				if (conn.getTransactionIsolation() < getTransactionLevel(config)) {
					conn.setTransactionIsolation(getTransactionLevel(config));
				}
				retVal = point.proceed();
				return retVal;
			} catch (SQLException e) {
				throw new ActiveRecordException(e);
			}
		}
		Boolean autoCommit = null;
		try {
			conn = config.getConnection();
			autoCommit = conn.getAutoCommit();
			config.setThreadLocalConnection(conn);
			conn.setTransactionIsolation(getTransactionLevel(config));
			conn.setAutoCommit(false);
			retVal = point.proceed();
			conn.commit();
		} catch (NestedTransactionHelpException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					LogKit.error(e1.getMessage(), e1);
				}
			}
		} catch (Throwable t) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					LogKit.error(e1.getMessage(), e1);
				}
			}
			throw Exceptions.unchecked(t);
		} finally {
			try {
				if (conn != null) {
					if (autoCommit != null) {
						conn.setAutoCommit(autoCommit);
					}
					conn.close();
				}
			} catch (SQLException t) {
				// can not throw exception here, otherwise the more important exception in previous catch block can not be thrown
				LogKit.error(t.getMessage(), t);
			} finally {
				// prevent memory leak
				config.removeThreadLocalConnection();
			}
		}
		return retVal;
	}

	/**
	 * 获取配置的事务级别
	 *
	 * @param config Config
	 * @return TransactionLevel
	 */
	private static int getTransactionLevel(Config config) {
		return config.getTransactionLevel();
	}

	/**
	 * 获取配置的TxConfig，可注解到class或者方法上
	 *
	 * @param pjp ProceedingJoinPoint
	 * @return Config
	 */
	private static Config getConfigWithTxConfig(ProceedingJoinPoint pjp) {
		MethodSignature ms = (MethodSignature) pjp.getSignature();
		Method method = ms.getMethod();
		TxConfig txConfig = method.getAnnotation(TxConfig.class);
		if (txConfig == null) {
			txConfig = pjp.getTarget().getClass().getAnnotation(TxConfig.class);
		}
		if (txConfig != null) {
			Config config = DbKit.getConfig(txConfig.value());
			if (config == null) {
				throw new IllegalArgumentException("Config not found with @TxConfig: " + txConfig.value());
			}
			return config;
		}
		return DbKit.getConfig();
	}

}
