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

package net.dreamlu.mica.captcha.core;

import java.io.OutputStream;
import java.util.function.Supplier;

/**
 * 验证码抽象
 *
 * @author L.cm
 */
public interface ICaptcha {

	/**
	 * 生成验证码
	 *
	 * @param supplier Supplier
	 * @return 验证码文字
	 */
	String generate(Supplier<OutputStream> supplier);

	/**
	 * 校验验证码
	 *
	 * @param code             验证码
	 * @param userInputCaptcha 用户输入的验证码
	 * @return 是否成功
	 */
	boolean validate(String code, String userInputCaptcha);

}
