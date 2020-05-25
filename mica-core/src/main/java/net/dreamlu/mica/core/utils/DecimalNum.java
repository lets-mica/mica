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
public class DecimalNum extends Number {
	@Getter
	private BigDecimal value;
	private final RoundingMode mode;

	public DecimalNum() {
		this(new BigDecimal(0));
	}

	public DecimalNum(BigDecimal decimal) {
		this(decimal, RoundingMode.UNNECESSARY);
	}

	public DecimalNum(BigDecimal decimal, RoundingMode roundingMode) {
		this.value = decimal;
		this.mode = roundingMode;
	}

	public static DecimalNum of(String decimal) {
		return new DecimalNum(new BigDecimal(decimal));
	}

	public static DecimalNum of(double decimal) {
		return new DecimalNum(BigDecimal.valueOf(decimal));
	}

	public static DecimalNum of(long decimal) {
		return new DecimalNum(BigDecimal.valueOf(decimal));
	}

	/**
	 * 设置小数位数
	 *
	 * @param newScale     小数位数
	 * @param roundingMode 模式
	 * @return DecimalNum
	 */
	public DecimalNum scale(int newScale, RoundingMode roundingMode) {
		this.value = value.setScale(newScale, roundingMode);
		return this;
	}

	/**
	 * 设置小数位数
	 *
	 * @param newScale 小数位数
	 * @return DecimalNum
	 */
	public DecimalNum scale(int newScale) {
		return scale(newScale, RoundingMode.HALF_EVEN);
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
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum divide(long decimal) {
		return divide(BigDecimal.valueOf(decimal));
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
	 * @param decimal 小数
	 * @return DecimalNum
	 */
	public DecimalNum divide(BigDecimal decimal) {
		this.value = value.divide(decimal);
		return this;
	}

	@Override
	public int intValue() {
		return value.intValue();
	}

	@Override
	public long longValue() {
		return value.longValue();
	}

	@Override
	public float floatValue() {
		return value.floatValue();
	}

	@Override
	public double doubleValue() {
		return value.doubleValue();
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
