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

package net.dreamlu.mica.core.utils;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 浮点型数据计算
 *
 * @author L.cm
 */
@Getter
public class DecimalNum extends Number {
	private BigDecimal value;

	public DecimalNum() {
		this(new BigDecimal(0));
	}

	public DecimalNum(BigDecimal decimal) {
		this.value = decimal;
	}

	/**
	 * 构造 DecimalNum
	 *
	 * @param decimal decimal
	 * @return DecimalNum
	 */
	public static DecimalNum of(BigDecimal decimal) {
		return new DecimalNum(decimal);
	}

	/**
	 * 构造 DecimalNum
	 *
	 * @param decimal decimal
	 * @return DecimalNum
	 */
	public static DecimalNum of(String decimal) {
		return of(new BigDecimal(decimal));
	}

	/**
	 * 构造 DecimalNum
	 *
	 * @param decimal decimal
	 * @return DecimalNum
	 */
	public static DecimalNum of(double decimal) {
		return of(BigDecimal.valueOf(decimal));
	}

	/**
	 * 构造 DecimalNum
	 *
	 * @param decimal decimal
	 * @return DecimalNum
	 */
	public static DecimalNum of(long decimal) {
		return of(BigDecimal.valueOf(decimal));
	}

	/**
	 * 加
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum add(String decimal) {
		return add(new BigDecimal(decimal));
	}

	/**
	 * 加
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum add(long decimal) {
		return add(BigDecimal.valueOf(decimal));
	}

	/**
	 * 加
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum add(double decimal) {
		return add(BigDecimal.valueOf(decimal));
	}

	/**
	 * 加
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum add(BigDecimal decimal) {
		this.value = value.add(decimal);
		return this;
	}

	/**
	 * 减
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum subtract(String decimal) {
		return subtract(new BigDecimal(decimal));
	}

	/**
	 * 减
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum subtract(long decimal) {
		return subtract(BigDecimal.valueOf(decimal));
	}

	/**
	 * 减
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum subtract(double decimal) {
		return subtract(BigDecimal.valueOf(decimal));
	}

	/**
	 * 减
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum subtract(BigDecimal decimal) {
		this.value = value.subtract(decimal);
		return this;
	}

	/**
	 * 乘
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum multiply(String decimal) {
		return multiply(new BigDecimal(decimal));
	}

	/**
	 * 乘
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum multiply(long decimal) {
		return multiply(BigDecimal.valueOf(decimal));
	}

	/**
	 * 乘
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum multiply(double decimal) {
		return multiply(BigDecimal.valueOf(decimal));
	}

	/**
	 * 乘
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum multiply(BigDecimal decimal) {
		this.value = value.multiply(decimal);
		return this;
	}

	/**
	 * 除
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum divide(String decimal) {
		return divide(new BigDecimal(decimal));
	}

	/**
	 * 除
	 *
	 * @param decimal      小数
	 * @param roundingMode 随机模型
	 * @return DecimalNum
	 */
	public DecimalNum divide(String decimal, RoundingMode roundingMode) {
		return divide(new BigDecimal(decimal), roundingMode);
	}

	/**
	 * 除
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum divide(long decimal) {
		return divide(BigDecimal.valueOf(decimal));
	}

	/**
	 * 除
	 *
	 * @param decimal      小数
	 * @param roundingMode 随机模型
	 * @return DecimalNum
	 */
	public DecimalNum divide(long decimal, RoundingMode roundingMode) {
		return divide(BigDecimal.valueOf(decimal), roundingMode);
	}

	/**
	 * 除
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum divide(double decimal) {
		return divide(BigDecimal.valueOf(decimal));
	}

	/**
	 * 除
	 *
	 * @param decimal      小数
	 * @param roundingMode 随机模型
	 * @return DecimalNum
	 */
	public DecimalNum divide(double decimal, RoundingMode roundingMode) {
		return divide(BigDecimal.valueOf(decimal), roundingMode);
	}

	/**
	 * 除
	 *
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum divide(BigDecimal decimal) {
		this.value = value.divide(decimal);
		return this;
	}

	/**
	 * 除
	 *
	 * @param decimal      小数
	 * @param roundingMode 随机模型
	 * @return DecimalNum
	 */
	public DecimalNum divide(BigDecimal decimal, RoundingMode roundingMode) {
		this.value = value.divide(decimal, roundingMode);
		return this;
	}

	/**
	 * 设置小数位数
	 *
	 * @param scale 小数位数
	 * @return DecimalNum
	 */
	public DecimalNum scale(int scale) {
		return scale(scale, RoundingMode.HALF_EVEN);
	}

	/**
	 * 设置小数位数
	 *
	 * @param scale        小数位数
	 * @param roundingMode 模式
	 * @return DecimalNum
	 */
	public DecimalNum scale(int scale, RoundingMode roundingMode) {
		this.value = value.setScale(scale, roundingMode);
		return this;
	}

	/**
	 * intValue
	 *
	 * @return 转为 int 值
	 */
	@Override
	public int intValue() {
		return value.intValue();
	}

	/**
	 * longValue
	 *
	 * @return 转为 long 值
	 */
	@Override
	public long longValue() {
		return value.longValue();
	}

	/**
	 * floatValue
	 *
	 * @return 转为 float 值
	 */
	@Override
	public float floatValue() {
		return value.floatValue();
	}

	/**
	 * doubleValue
	 *
	 * @return 转为 double 值
	 */
	@Override
	public double doubleValue() {
		return value.doubleValue();
	}

	/**
	 * toString
	 *
	 * @return 转为字符串
	 */
	@Override
	public String toString() {
		return value.toString();
	}

}
