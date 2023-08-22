package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.core.utils.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * uuid 测试
 *
 * @author L.cm
 */
class UUIDTest {

    @Test
    void test() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        Set<String> uuidSet = ConcurrentHashMap.newKeySet();
        int size = 100_0000;
        for (int i = 0; i < size; i++) {
            service.submit(() -> {
                String nanoId = StringUtil.getNanoId();
                if (uuidSet.contains(nanoId)) {
                    System.out.println("-----------存在冲突-------");
                } else {
                    uuidSet.add(nanoId);
                }
            });
        }
        ThreadUtil.sleep(TimeUnit.SECONDS, 5);
        Assertions.assertEquals(size, uuidSet.size());
        service.shutdown();
    }

}
