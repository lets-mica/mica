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

package net.dreamlu.mica.core.exception;

import net.dreamlu.mica.core.result.IResultCode;
import net.dreamlu.mica.core.result.R;
import org.springframework.lang.Nullable;

/**
 * 业务异常
 *
 * @author L.cm
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 2359767895161832954L;

	@Nullable
	private final R<?> result;

	public ServiceException(R<?> result) {
		super(result.getMsg());
		this.result = result;
	}

	public ServiceException(IResultCode rCode) {
		this(rCode, rCode.getMsg());
	}

	public ServiceException(IResultCode rCode, String message) {
		super(message);
		this.result = R.fail(rCode, message);
	}

	public ServiceException(String message) {
		super(message);
		this.result = null;
	}

	public ServiceException(Throwable cause) {
		this(cause.getMessage(), cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		doFillInStackTrace();
		this.result = null;
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public <T> R<T> getResult() {
		return (R<T>) result;
	}

	/**
	 * 提高性能
	 * @return Throwable
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

	public Throwable doFillInStackTrace() {
		return super.fillInStackTrace();
	}

}
