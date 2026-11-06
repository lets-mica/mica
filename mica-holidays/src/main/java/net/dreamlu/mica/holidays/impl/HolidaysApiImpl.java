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
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.holidays.config.HolidaysApiProperties;
import net.dreamlu.mica.holidays.core.DaysType;
import net.dreamlu.mica.holidays.core.HolidaysApi;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 节假日实现
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class HolidaysApiImpl implements HolidaysApi, InitializingBean {
	/**
	 * 内置数据的目录
	 */
	private static final String DATA_DIR = "data/";
	/**
	 * 内置数据的匹配规则，如：classpath*:data/*_data.json
	 */
	private static final String DATA_PATTERN = "classpath*:" + DATA_DIR + "*_data.json";
	/**
	 * 内置数据的文件名规则，如：2027_data.json
	 */
	private static final Pattern YEAR_FILE_PATTERN = Pattern.compile("(?:^|[/\\\\])(\\d{4})_data\\.json$");

	/**
	 * 存储节假日
	 */
	private final Map<Integer, Map<String, Byte>> yearDataMap = new ConcurrentHashMap<>();
	/**
	 * 没有内置数据的年份，避免重复查找
	 */
	private final Set<Integer> missingYears = ConcurrentHashMap.newKeySet();
	private final ResourceLoader resourceLoader;
	private final HolidaysApiProperties properties;

	@Override
	public DaysType getDaysType(LocalDate localDate) {
		int year = localDate.getYear();
		Map<String, Byte> dataMap = yearDataMap.get(year);
		if (dataMap == null) {
			// 扩展的配置可能已经被修改，重新加载一次，避免重启
			if (checkProperties(year)) {
				loadExtDataProperties();
				dataMap = yearDataMap.get(year);
			}
			// 内置数据兜底：按需加载（native 镜像等无法枚举 classpath 时生效）
			if (dataMap == null) {
				dataMap = loadBuiltInData(year);
			}
		}
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
		// 内置的配置：扫描 classpath:data/*_data.json 自动识别年份，无需维护年份列表
		loadBuiltInData();
		// 自行扩展的配置
		loadExtDataProperties();
	}

	/**
	 * 加载内置的节假日数据，自动扫描 classpath 下 data 目录中的 *_data.json
	 */
	private void loadBuiltInData() {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
		Resource[] resources;
		try {
			resources = resolver.getResources(DATA_PATTERN);
		} catch (Exception e) {
			// 扫描失败（如 native 镜像不支持枚举 classpath），后续按需加载兜底
			log.warn("扫描内置节假日数据失败，将按需加载：{}", e.getMessage());
			return;
		}
		for (Resource resource : resources) {
			String filename = resource.getFilename();
			if (filename == null) {
				continue;
			}
			Matcher matcher = YEAR_FILE_PATTERN.matcher(filename);
			if (!matcher.find()) {
				continue;
			}
			int year = Integer.parseInt(matcher.group(1));
			Map<String, Byte> dataMap = readBuiltInData(resource);
			if (dataMap != null) {
				// 同一年份有多个数据源时，classpath 靠前的优先
				yearDataMap.putIfAbsent(year, dataMap);
			}
		}
		if (!yearDataMap.isEmpty()) {
			log.info("加载内置节假日数据完成，年份：{}", yearDataMap.keySet().stream().sorted().toList());
		}
	}

	/**
	 * 按需加载某一年的内置数据，用于 native 镜像等无法枚举 classpath 的场景
	 *
	 * @param year 年
	 * @return 该年的数据，没有则返回 null
	 */
	private Map<String, Byte> loadBuiltInData(int year) {
		if (missingYears.contains(year)) {
			return null;
		}
		return yearDataMap.computeIfAbsent(year, y -> {
			Resource resource = resourceLoader.getResource("classpath:" + DATA_DIR + y + "_data.json");
			Map<String, Byte> dataMap = readBuiltInData(resource);
			if (dataMap == null) {
				missingYears.add(y);
			}
			return dataMap;
		});
	}

	/**
	 * 读取内置的节假日数据文件，文件不存在时返回 null
	 *
	 * @param resource 资源
	 * @return 数据，文件不存在时返回 null
	 */
	private Map<String, Byte> readBuiltInData(Resource resource) {
		try (InputStream inputStream = resource.getInputStream()) {
			return JsonUtil.readMap(inputStream, Byte.class);
		} catch (FileNotFoundException e) {
			// 没有该年份的数据文件
			return null;
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 加载扩展的配置
	 */
	private void loadExtDataProperties() {
		List<HolidaysApiProperties.ExtData> extDataList = properties.getExtData();
		for (HolidaysApiProperties.ExtData extData : extDataList) {
			String dataPath = extData.getDataPath();
			Resource resource = resourceLoader.getResource(dataPath);
			try (InputStream inputStream = resource.getInputStream()) {
				Map<String, Byte> dataMap = JsonUtil.readMap(inputStream, Byte.class);
				// 扩展的配置优先于内置的数据
				yearDataMap.put(extData.getYear(), dataMap);
			} catch (IOException e) {
				throw Exceptions.unchecked(e);
			}
		}
	}

	/**
	 * 检查配置，是否有配置该年的配置
	 *
	 * @param year 年
	 * @return 是否有数据
	 */
	private boolean checkProperties(int year) {
		List<HolidaysApiProperties.ExtData> dataList = properties.getExtData();
		if (dataList.isEmpty()) {
			return false;
		}
		return dataList.stream().anyMatch(data -> year == data.getYear());
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
