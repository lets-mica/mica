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

package net.dreamlu.mica.lock.client;

import net.dreamlu.mica.core.function.CheckedSupplier;
import net.dreamlu.mica.lock.annotation.LockType;

import java.util.concurrent.TimeUnit;

/**
 * 锁客户端
 *
 * @author L.cm
 */
public interface RedisLockClient {

	/**
	 * 尝试获取锁
	 *
	 * @param lockName  锁名
	 * @param lockType  锁类型
	 * @param waitTime  等待时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间
	 * @param timeUnit  时间参数
	 * @return 是否成功
	 * @throws InterruptedException InterruptedException
	 */
	boolean tryLock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;

	/**
	 * 解锁
	 *
	 * @param lockName 锁名
	 * @param lockType 锁类型
	 */
	void unLock(String lockName, LockType lockType);

	/**
	 * 自定获取锁后执行方法
	 *
	 * @param lockName  锁名
	 * @param lockType  锁类型
	 * @param waitTime  等待锁超时时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
	 * @param timeUnit  时间单位
	 * @param supplier  获取锁后的回调
	 * @return 返回的数据
	 */
	<T> T lock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier);

	/**
	 * 公平锁
	 *
	 * @param lockName  锁名
	 * @param waitTime  等待锁超时时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
	 * @param supplier  获取锁后的回调
	 * @return 返回的数据
	 */
	default <T> T lockFair(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
		return lock(lockName, LockType.FAIR, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
	}

	/**
	 * 可重入锁
	 *
	 * @param lockName  锁名
	 * @param waitTime  等待锁超时时间
	 * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
	 * @param supplier  获取锁后的回调
	 * @return 返回的数据
	 */
	default <T> T lockReentrant(String lockName, long waitTime, long leaseTime, CheckedSupplier<T> supplier) {
		return lock(lockName, LockType.REENTRANT, waitTime, leaseTime, TimeUnit.SECONDS, supplier);
	}

}
