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

package net.dreamlu.mica.captcha;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * 验证码对象封装，用于传递到前端
 *
 * @author L.cm
 */
@Getter
@ApiModel("验证码模型")
public class Captcha implements Serializable {
	/**
	 * 用于传给前端，校验时携带
 	 */
	@ApiModelProperty("验证码唯一id")
	private final String uuid;
	/**
	 * 图片 base64
	 */
	@ApiModelProperty("验证码图片（base64）")
	private final String base64;

	Captcha(String uuid, String base64) {
		this.uuid = uuid;
		this.base64 = base64;
	}
}
