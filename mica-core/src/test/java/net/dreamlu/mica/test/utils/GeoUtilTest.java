package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.geo.GeoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * geo 测试
 *
 * @author L.cm
 */
public class GeoUtilTest {

	@Test
	public void test() {
		// 坐标来自高德地图： https://lbs.amap.com/api/javascript-api/example/calcutation/calculate-distance-between-two-markers
		double distance = GeoUtil.getDistance(116.368904, 39.923423, 116.387271, 39.922501);
		// 距离为 1571
		Assertions.assertEquals(1571, (long) distance);
	}

	@Test
	public void testGps() {
		int latitude = 9577595;
		int longitude = 38383456;
		System.out.println(GeoUtil.formatGeo(latitude));
		System.out.println(GeoUtil.formatGeo(longitude));
		double lat = GeoUtil.getGpsValue(latitude);
		double lon = GeoUtil.getGpsValue(longitude);
		System.out.println(GeoUtil.formatGeo(lat));
		System.out.println(GeoUtil.formatGeo(lon));
		System.out.printf("lat:%s\nlon:%s", lat, lon);
	}

}
