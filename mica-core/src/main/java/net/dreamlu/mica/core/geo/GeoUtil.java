package net.dreamlu.mica.core.geo;

import java.text.DecimalFormat;

/**
 * 位置工具类
 *
 * @author JourWon、hutool、L.cm
 */
public class GeoUtil {
	/**
	 * 地球的半径 (m)
	 */
	public static final double EARTH_RADIUS = 6378137;
	/**
	 * 圆周率π
	 */
	private static final double PI = 3.1415926535897932384626D;
	/**
	 * 火星坐标系与百度坐标系转换的中间量
	 */
	private static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0D;
	/**
	 * 地球半径（Krasovsky 1940）
	 */
	public static final double RADIUS = 6378245.0D;
	/**
	 * 修正参数（偏率ee）
	 */
	public static final double CORRECTION_PARAM = 0.00669342162296594323D;
	/**
	 * 经纬度格式化为字符串
	 */
	public static final DecimalFormat FORMAT = new DecimalFormat("#.########");

	/**
	 * 根据经纬度计算两点之间的距离 (m)
	 *
	 * @param lon1 位置 1 的经度
	 * @param lat1 位置 1 的纬度
	 * @param lon2 位置 2 的经度
	 * @param lat2 位置 2 的纬度
	 * @return 返回距离
	 */
	public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
		double radLat1 = radian(lat1);
		double radLat2 = radian(lat2);
		double a = radLat1 - radLat2;
		double b = radian(lon1) - radian(lon2);
		return (2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
			+ Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2))))
			* EARTH_RADIUS;
	}

	/**
	 * 将整数形式的经纬度转换为十进制度格式，因为部分设备采集上来的为十进制形式的坐标
	 *
	 * @param coordinate 整数形式的经度或纬度
	 * @return 十进制度格式的经纬度
	 */
	public static double getGpsValue(int coordinate) {
		int degrees = coordinate / (3600 * 100);
		int remainder = coordinate % (3600 * 100);
		int minutes = remainder / (60 * 100);
		remainder = remainder % (60 * 100);
		double seconds = remainder / 100.0;
		// 将分和秒转换为度的小数部分
		return degrees + (minutes / 60.0) + (seconds / 3600.0);
	}

	/**
	 * 判断坐标是否在国外<br>
	 * 火星坐标系 (GCJ-02)只对国内有效，国外无需转换
	 *
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 坐标是否在国外
	 */
	public static boolean isOutOfChina(double lng, double lat) {
		return (lng < 72.004 || lng > 137.8347) || (lat < 0.8293 || lat > 55.8271);
	}

	/**
	 * WGS84 转换为 火星坐标系 (GCJ-02)
	 *
	 * @param lon 经度值
	 * @param lat 纬度值
	 * @return 火星坐标 (GCJ-02)
	 */
	public static GeoPoint wgs84ToGcj02(double lon, double lat) {
		return new GeoPoint(lon, lat).offset(offset(lon, lat, true));
	}

	/**
	 * WGS84 坐标转为 百度坐标系 (BD-09) 坐标
	 *
	 * @param lon 经度值
	 * @param lat 纬度值
	 * @return bd09 坐标
	 */
	public static GeoPoint wgs84ToBd09(double lon, double lat) {
		final GeoPoint gcj02 = wgs84ToGcj02(lon, lat);
		return gcj02ToBd09(gcj02.getLon(), gcj02.getLat());
	}

	/**
	 * 火星坐标系 (GCJ-02) 转换为 WGS84
	 *
	 * @param lon 经度坐标
	 * @param lat 纬度坐标
	 * @return WGS84 坐标
	 */
	public static GeoPoint gcj02ToWgs84(double lon, double lat) {
		return new GeoPoint(lon, lat).offset(offset(lon, lat, false));
	}

	/**
	 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
	 *
	 * @param lon 经度值
	 * @param lat 纬度值
	 * @return BD-09 坐标
	 */
	public static GeoPoint gcj02ToBd09(double lon, double lat) {
		double z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
		double theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * X_PI);
		double bd_lng = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new GeoPoint(bd_lng, bd_lat);
	}

	/**
	 * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
	 * 即 百度 转 谷歌、高德
	 *
	 * @param lon 经度值
	 * @param lat 纬度值
	 * @return GCJ-02 坐标
	 */
	public static GeoPoint bd09ToGcj02(double lon, double lat) {
		double x = lon - 0.0065;
		double y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new GeoPoint(gg_lon, gg_lat);
	}

	/**
	 * 百度坐标系 (BD-09) 与 WGS84 的转换
	 *
	 * @param lon 经度值
	 * @param lat 纬度值
	 * @return WGS84坐标
	 */
	public static GeoPoint bd09toWgs84(double lon, double lat) {
		final GeoPoint gcj02 = bd09ToGcj02(lon, lat);
		return gcj02ToWgs84(gcj02.getLon(), gcj02.getLat());
	}

	/**
	 * WGS84 坐标转为 墨卡托投影
	 *
	 * @param lon 经度值
	 * @param lat 纬度值
	 * @return 墨卡托投影
	 */
	public static GeoPoint wgs84ToMercator(double lon, double lat) {
		double x = lon * 20037508.342789244 / 180;
		double y = Math.log(Math.tan((90 + lat) * PI / 360)) / (PI / 180);
		y = y * 20037508.342789244 / 180;
		return new GeoPoint(x, y);
	}

	/**
	 * 墨卡托投影 转为 WGS84 坐标
	 *
	 * @param mercatorX 墨卡托X坐标
	 * @param mercatorY 墨卡托Y坐标
	 * @return WGS84 坐标
	 */
	public static GeoPoint mercatorToWgs84(double mercatorX, double mercatorY) {
		double x = mercatorX / 20037508.342789244 * 180;
		double y = mercatorY / 20037508.342789244 * 180;
		y = 180 / PI * (2 * Math.atan(Math.exp(y * PI / 180)) - PI / 2);
		return new GeoPoint(x, y);
	}

	/**
	 * 格式化经纬度
	 *
	 * @param value value
	 * @return 格式化经纬度
	 */
	public static String formatGeo(double value) {
		return FORMAT.format(value);
	}

	/**
	 * WGS84 与 火星坐标系 (GCJ-02)转换的偏移算法（非精确）
	 *
	 * @param lon    经度值
	 * @param lat    纬度值
	 * @param isPlus 是否正向偏移：WGS84转GCJ-02使用正向，否则使用反向
	 * @return 偏移坐标
	 */
	private static GeoPoint offset(double lon, double lat, boolean isPlus) {
		double dlon = transLon(lon - 105.0, lat - 35.0);
		double dlat = transLat(lon - 105.0, lat - 35.0);

		double magic = Math.sin(lat / 180.0 * PI);
		magic = 1 - CORRECTION_PARAM * magic * magic;
		final double sqrtMagic = Math.sqrt(magic);

		dlon = (dlon * 180.0) / (RADIUS / sqrtMagic * Math.cos(lat / 180.0 * PI) * PI);
		dlat = (dlat * 180.0) / ((RADIUS * (1 - CORRECTION_PARAM)) / (magic * sqrtMagic) * PI);

		if (isPlus) {
			return new GeoPoint(dlon, dlat);
		} else {
			return new GeoPoint(-dlon, -dlat);
		}
	}

	/**
	 * 计算经度坐标
	 *
	 * @param lon 经度坐标
	 * @param lat 纬度坐标
	 * @return ret 计算完成后的
	 */
	private static double transLon(double lon, double lat) {
		double ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
		ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lon * PI) + 40.0 * Math.sin(lon / 3.0 * PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lon / 12.0 * PI) + 300.0 * Math.sin(lon / 30.0 * PI)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * 计算纬度坐标
	 *
	 * @param lon 经度
	 * @param lat 纬度
	 * @return ret 计算完成后的
	 */
	private static double transLat(double lon, double lat) {
		double ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat
			+ 0.2 * Math.sqrt(Math.abs(lon));
		ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double radian(double d) {
		return d * PI / 180.0;
	}

}
