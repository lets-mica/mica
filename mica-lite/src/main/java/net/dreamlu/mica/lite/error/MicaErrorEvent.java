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

package net.dreamlu.mica.lite.error;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * mica 服务 异常 事件
 *
 * @author L.cm
 */
@Getter
@Setter
@ToString
public class MicaErrorEvent implements Serializable {

	/**
	 * 唯一 id，捕捉异常入库时初始化
	 */
	@Nullable
	private String id;
	/**
	 * 应用名
	 */
	@Nullable
	private String appName;
	/**
	 * 环境
	 */
	@Nullable
	private String env;
	/**
	 * git/svn commit id
	 */
	@Nullable
	private String commitId;
	/**
	 * 异常类型
	 */
	@Nullable
	private ErrorType errorType;
	/**
	 * 远程ip 主机名
	 */
	@Nullable
	private String remoteHost;
	/**
	 * 请求id
	 */
	@Nullable
	private String requestId;
	/**
	 * 请求方法名
	 */
	@Nullable
	private String requestMethod;
	/**
	 * 请求url
	 */
	@Nullable
	private String requestUrl;
	/**
	 * 请求ip
	 */
	@Nullable
	private String requestIp;
	/**
	 * 堆栈信息
	 */
	@Nullable
	private String stackTrace;
	/**
	 * 异常名
	 */
	@Nullable
	private String exceptionName;
	/**
	 * 异常消息
	 */
	@Nullable
	private String message;
	/**
	 * 类名
	 */
	@Nullable
	private String className;
	/**
	 * 文件名
	 */
	@Nullable
	private String fileName;
	/**
	 * 方法名
	 */
	@Nullable
	private String methodName;
	/**
	 * 代码行数
	 */
	@Nullable
	private Integer lineNumber;
	/**
	 * 异常时间
	 */
	private LocalDateTime createdAt;

}
