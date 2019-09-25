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

package net.dreamlu.mica.feign;

import com.fasterxml.jackson.databind.JsonNode;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.core.utils.ObjectUtil;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * mica fullBack 代理处理
 *
 * @author L.cm
 */
@Slf4j
@AllArgsConstructor
public class MicaFeignFallback<T> implements MethodInterceptor {
	private final Class<T> targetType;
	private final String targetName;
	private final Throwable cause;

	@Nullable
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		String errorMessage = cause.getMessage();
		log.error("MicaFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);
		Class<?> returnType = method.getReturnType();
		// 集合类型反馈空集合
		if (List.class == returnType || Collection.class == returnType) {
			return Collections.emptyList();
		}
		if (Set.class == returnType) {
			return Collections.emptySet();
		}
		if (Map.class == returnType) {
			return Collections.emptyMap();
		}
		// 暂时不支持 flux，rx，异步等，返回值不是 R，直接返回 null。
		if (R.class != returnType) {
			return null;
		}
		// 非 FeignException，直接返回 【100009】请求被拒绝
		if (!(cause instanceof FeignException)) {
			return R.fail(SystemCode.REQ_REJECT, errorMessage);
		}
		FeignException exception = (FeignException) cause;
		byte[] content = exception.content();
		// 如果返回的数据为空
		if (ObjectUtil.isEmpty(content)) {
			return R.fail(SystemCode.REQ_REJECT, errorMessage);
		}
		// 转换成 jsonNode 读取，因为直接转换，可能 对方放回的并 不是 R 的格式。
		JsonNode resultNode = JsonUtil.readTree(content);
		// 判断是否 R 格式 返回体
		if (resultNode.has("code")) {
			return JsonUtil.getInstance().convertValue(resultNode, R.class);
		}
		return R.fail(resultNode.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MicaFeignFallback<?> that = (MicaFeignFallback<?>) o;
		return targetType.equals(that.targetType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetType);
	}

}
