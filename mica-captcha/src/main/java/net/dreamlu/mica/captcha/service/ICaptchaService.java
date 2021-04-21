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

package net.dreamlu.mica.captcha.service;

import net.dreamlu.mica.captcha.vo.CaptchaVo;
import net.dreamlu.mica.core.utils.Base64Util;
import net.dreamlu.mica.core.utils.StringUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.OutputStream;

/**
 * 验证码服务
 *
 * @author L.cm
 */
public interface ICaptchaService {

	/**
	 * 生成验证码
	 *
	 * @param uuid         自定义缓存的 uuid
	 * @param outputStream OutputStream
	 */
	void generate(String uuid, OutputStream outputStream);

	/**
	 * 生成验证码
	 *
	 * @param uuid 自定义缓存的 uuid
	 * @return bytes
	 */
	default byte[] generateBytes(String uuid) {
		FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
		this.generate(uuid, outputStream);
		return outputStream.toByteArray();
	}

	/**
	 * 生成验证码
	 *
	 * @param uuid 自定义缓存的 uuid
	 * @return ByteArrayResource
	 */
	default ByteArrayResource generateByteResource(String uuid) {
		return new ByteArrayResource(this.generateBytes(uuid));
	}

	/**
	 * 生成验证码 base64 字符串
	 *
	 * @param uuid 自定义缓存的 uuid
	 * @return base64 图片
	 */
	default String generateBase64(String uuid) {
		FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
		this.generate(uuid, outputStream);
		return "data:image/jpeg;base64," + Base64Util.encodeToString(outputStream.toByteArray());
	}

	/**
	 * 生成验证码 base64 CaptchaVo
	 *
	 * @return CaptchaVo
	 */
	default CaptchaVo generateBase64Vo() {
		return generateBase64Vo(StringUtil.getUUID());
	}

	/**
	 * 生成验证码 base64 CaptchaVo
	 *
	 * @param uuid 自定义缓存的 uuid
	 * @return CaptchaVo
	 */
	default CaptchaVo generateBase64Vo(String uuid) {
		return new CaptchaVo(uuid, this.generateBase64(uuid));
	}

	/**
	 * 生成验证码
	 *
	 * @param uuid captcha uuid
	 * @return {ResponseEntity}
	 */
	default ResponseEntity<Resource> generateResponseEntity(String uuid) {
		return new ResponseEntity<>(this.generateByteResource(uuid), this.getCaptchaHeaders(), HttpStatus.OK);
	}

	/**
	 * 图片输出的头，避免验证码被缓存
	 *
	 * @return {HttpHeaders}
	 */
	default HttpHeaders getCaptchaHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setPragma("no-cache");
		headers.setCacheControl("no-cache");
		headers.setExpires(0);
		headers.setContentType(MediaType.IMAGE_JPEG);
		return headers;
	}

	/**
	 * 校验验证码
	 *
	 * @param uuid             自定义缓存的 uuid
	 * @param userInputCaptcha 用户输入的图形验证码
	 * @return 是否校验成功
	 */
	boolean validate(String uuid, String userInputCaptcha);

}
