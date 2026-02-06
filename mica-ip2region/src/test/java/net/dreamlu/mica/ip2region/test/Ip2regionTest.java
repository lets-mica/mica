package net.dreamlu.mica.ip2region.test;

import net.dreamlu.mica.ip2region.config.Ip2regionConfiguration;
import net.dreamlu.mica.ip2region.config.Ip2regionProperties;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import net.dreamlu.mica.ip2region.impl.Ip2regionSearcherImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * ip2region 测试
 *
 * @author L.cm
 */
class Ip2regionTest {
	private Ip2regionSearcher searcher;

	@BeforeEach
	public void setup() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		Ip2regionConfiguration configuration = new Ip2regionConfiguration();
		searcher = configuration.ip2regionSearcher(context, new Ip2regionProperties());
		((Ip2regionSearcherImpl) searcher).afterPropertiesSet();
	}

	@Test
	void getRegion() {
		System.out.println(searcher.memorySearch("220.248.12.158"));
		System.out.println(searcher.memorySearch("222.240.36.135"));
		System.out.println(searcher.memorySearch("172.30.13.97"));
		System.out.println(searcher.memorySearch("223.26.64.0"));
		System.out.println(searcher.memorySearch("223.26.128.0"));
		System.out.println(searcher.memorySearch("223.26.67.0"));
		System.out.println(searcher.memorySearch("223.29.220.0"));
		System.out.println(searcher.memorySearch("82.120.124.0"));
	}

	@Test
	void getRegionV6() {
		System.out.println(searcher.memorySearch("::ffff:1111:2222"));
		System.out.println(searcher.memorySearch("2001:db8::ffff:1111:2222"));
		System.out.println(searcher.memorySearch("::1"));
		System.out.println(searcher.memorySearch("2406:840:20::1"));
		System.out.println(searcher.memorySearch("2c0f:feb0:a::"));
		System.out.println(searcher.memorySearch("240e:109:8047::"));
		System.out.println(searcher.memorySearch("1111:1111:1111::1111"));
	}

	@Test
	void test2() {
		IpInfo ipInfo = searcher.memorySearch("127.0.0.1");
		Assertions.assertNotNull(ipInfo);
	}

}
