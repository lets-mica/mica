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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.function.CheckedSupplier;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.lock.annotation.LockType;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 锁客户端
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLockClientImpl implements RedisLockClient {
	private final RedissonClient redissonClient;

	@Override
	public boolean tryLock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
		RLock lock = getLock(lockName, lockType);
		return lock.tryLock(waitTime, leaseTime, timeUnit);
	}

	@Override
	public void unLock(String lockName, LockType lockType) {
		RLock lock = getLock(lockName, lockType);
		lock.unlock();
	}

	private RLock getLock(String lockName, LockType lockType) {
		RLock lock;
		if (LockType.REENTRANT == lockType) {
			lock = redissonClient.getLock(lockName);
		} else {
			lock = redissonClient.getFairLock(lockName);
		}
		return lock;
	}

	@Override
	public <T> T lock(String lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit, CheckedSupplier<T> supplier) {
		try {
			boolean result = this.tryLock(lockName, lockType, waitTime, leaseTime, timeUnit);
			if (result) {
				return supplier.get();
			}
		} catch (Throwable e) {
			throw Exceptions.unchecked(e);
		} finally {
			this.unLock(lockName, lockType);
		}
		return null;
	}

}
