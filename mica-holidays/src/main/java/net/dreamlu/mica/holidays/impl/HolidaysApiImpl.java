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

package net.dreamlu.mica.holidays.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.holidays.config.HolidaysApiProperties;
import net.dreamlu.mica.holidays.core.DaysType;
import net.dreamlu.mica.holidays.core.HolidaysApi;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节假日实现
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class HolidaysApiImpl implements HolidaysApi, InitializingBean {
	/**
	 * 存储节假日
	 */
	private static final Map<Integer, Map<String, Byte>> YEAR_DATA_MAP = new HashMap<>();
	private final ResourceLoader resourceLoader;
	private final HolidaysApiProperties properties;

	@Override
	public DaysType getDaysType(LocalDate localDate) {
		int year = localDate.getYear();
		Map<String, Byte> dataMap = YEAR_DATA_MAP.get(year);
		// 对于没有数据的，我们按正常的周六日来判断，
		if (dataMap == null) {
			log.error("没有对应年:[{}]的数据，请升级或者自行维护数据！", year);
			return isWeekDay(localDate);
		}
		// 日期信息
		int monthValue = localDate.getMonthValue();
		int dayOfMonth = localDate.getDayOfMonth();
		// 月份和日期
		String monthAndDay = String.format("%02d%02d", monthValue, dayOfMonth);
		Byte result = dataMap.get(monthAndDay);
		if (result != null) {
			return DaysType.from(result);
		} else {
			return isWeekDay(localDate);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		int[] years = new int[]{2019, 2020, 2021, 2022, 2023, 2024, 2025};
		for (int year : years) {
			Resource resource = resourceLoader.getResource("classpath:data/" + year + "_data.json");
			try (InputStream inputStream = resource.getInputStream()) {
				Map<String, Byte> dataMap = JsonUtil.readMap(inputStream, Byte.class);
				YEAR_DATA_MAP.put(year, dataMap);
			}
		}
		List<HolidaysApiProperties.ExtData> extDataList = properties.getExtData();
		for (HolidaysApiProperties.ExtData extData : extDataList) {
			String dataPath = extData.getDataPath();
			Resource resource = resourceLoader.getResource(dataPath);
			try (InputStream inputStream = resource.getInputStream()) {
				Map<String, Byte> dataMap = JsonUtil.readMap(inputStream, Byte.class);
				YEAR_DATA_MAP.put(extData.getYear(), dataMap);
			}
		}
	}

	/**
	 * 判断是否工作日
	 *
	 * @param localDate LocalDate
	 * @return DaysType
	 */
	private static DaysType isWeekDay(LocalDate localDate) {
		int week = localDate.getDayOfWeek().getValue();
		return week == 6 || week == 7 ? DaysType.REST_DAYS : DaysType.WEEKDAYS;
	}

}
