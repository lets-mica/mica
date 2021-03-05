package net.dreamlu.mica.ip2region.test;

import net.dreamlu.mica.ip2region.config.Ip2regionConfiguration;
import net.dreamlu.mica.ip2region.config.Ip2regionProperties;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import net.dreamlu.mica.ip2region.impl.Ip2regionSearcherImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * ip2region 测试
 *
 * @author L.cm
 */
public class Ip2regionTest {
	private Ip2regionSearcher searcher;

	@Before
	public void setup() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		Ip2regionConfiguration configuration = new Ip2regionConfiguration();
		searcher = configuration.ip2regionSearcher(context, new Ip2regionProperties());
		((Ip2regionSearcherImpl) searcher).afterPropertiesSet();
	}

	@Test
	public void getRegion() throws Exception {
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
	public void test2() {
		IpInfo ipInfo = searcher.btreeSearch("127.0.0.1");
		Assert.assertNotNull(ipInfo);
	}

}
