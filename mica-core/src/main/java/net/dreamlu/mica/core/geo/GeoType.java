/**
 * Copyright (c) 2018-2028, DreamLu 卢春梦 (qq596392912@gmail.com).
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

package net.dreamlu.mica.core.geo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 各坐标系之间的转换工具类
 * <p>
 * 参考：<a href="https://github.com/JourWon/coordinate-transform">coordinate-transform</a>
 * <p>
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系。
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外,谷歌中国地图采用的是GCJ02地理坐标系。)
 * <p>
 * GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。
 * 出于国家安全考虑，国内所有导航电子地图必须使用国家测绘局制定的加密坐标系统，即将一个真实的经纬度坐标加密成一个不正确的经纬度坐标。
 * <p>
 * BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系。搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 * <p>
 * 高德MapABC地图API 火星坐标
 * 腾讯搜搜地图API 火星坐标
 * 阿里云地图API 火星坐标
 * 灵图51ditu地图API 火星坐标
 * <p>
 * 百度地图API 百度坐标
 * 搜狐搜狗地图API 搜狗坐标
 * 图吧MapBar地图API 图吧坐标
 *
 * @author JourWon、hutool、L.cm
 */
@Getter
@RequiredArgsConstructor
public enum GeoType {

	/**
	 * WGS84
	 */
	WGS84("WGS84", "地球坐标系，国际通用坐标系") {
		@Override
		public GeoPoint toWGS84(double lon, double lat) {
			return new GeoPoint(lon, lat);
		}

		@Override
		public GeoPoint toGCJ02(double lon, double lat) {
			return GeoUtil.wgs84ToGcj02(lon, lat);
		}

		@Override
		public GeoPoint toBD09(double lon, double lat) {
			return GeoUtil.wgs84ToBd09(lon, lat);
		}
	},
	GCJ02("GCJ02", "火星坐标系，高德、腾讯、阿里等使用") {
		@Override
		public GeoPoint toWGS84(double lon, double lat) {
			return GeoUtil.gcj02ToWgs84(lon, lat);
		}

		@Override
		public GeoPoint toGCJ02(double lon, double lat) {
			return new GeoPoint(lon, lat);
		}

		@Override
		public GeoPoint toBD09(double lon, double lat) {
			return GeoUtil.gcj02ToBd09(lon, lat);
		}
	},
	BD09("BD09", "百度坐标系，百度、搜狗等使用") {
		@Override
		public GeoPoint toWGS84(double lon, double lat) {
			return GeoUtil.bd09toWgs84(lon, lat);
		}

		@Override
		public GeoPoint toGCJ02(double lon, double lat) {
			return GeoUtil.bd09ToGcj02(lon, lat);
		}

		@Override
		public GeoPoint toBD09(double lon, double lat) {
			return new GeoPoint(lon, lat);
		}
	};

	@JsonValue
	private final String type;
	private final String desc;

	/**
	 * 转换成 地球坐标系
	 *
	 * @param lon lon
	 * @param lat lat
	 * @return GeoPoint
	 */
	public abstract GeoPoint toWGS84(double lon, double lat);

	/**
	 * 转换成 火星坐标系
	 *
	 * @param lon lon
	 * @param lat lat
	 * @return GeoPoint
	 */
	public abstract GeoPoint toGCJ02(double lon, double lat);

	/**
	 * 转换成 百度坐标系
	 *
	 * @param lon lon
	 * @param lat lat
	 * @return GeoPoint
	 */
	public abstract GeoPoint toBD09(double lon, double lat);


	/**
	 * 获取坐标系
	 *
	 * @param type type 坐标系类型
	 * @return GeoType
	 */
	@JsonCreator
	public static GeoType getGeoType(String type) {
		for (GeoType geoType : values()) {
			if (geoType.type.equalsIgnoreCase(type)) {
				return geoType;
			}
		}
		throw new IllegalArgumentException("未知的坐标系类型" + type);
	}

}
