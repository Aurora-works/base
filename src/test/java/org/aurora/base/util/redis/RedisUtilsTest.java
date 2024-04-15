package org.aurora.base.util.redis;

import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.extension.TimingExtension;
import org.aurora.base.service.sys.SysTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@ExtendWith(TimingExtension.class)
@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class RedisUtilsTest {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysTableService tableService;

    @Test
    void testObj() {
        SysTable table = tableService.findById(701L);
        System.out.println(table);
        redisUtils.set("key-table", table);
        System.out.println(redisUtils.getStr("key-table"));
    }

    @Test
    void testObj1() {
        Object table = redisUtils.get("key-table");
        SysTable table1 = (SysTable) table;
        System.out.println(table1);
        System.out.println(table1.getCreateUser());
        System.out.println(table1.getLastUser());
    }

    @Test
    void test() {
        redisUtils.set("key-1", "value-1");
        redisUtils.setStr("key-2", "value-2");
        redisUtils.set("key-3", "value-3", 10, TimeUnit.SECONDS);
        redisUtils.setStr("key-4", "value-4", 10, TimeUnit.MINUTES);
        print();
    }

    @Test
    void testExpire() {
        print();
        redisUtils.expire("key-1", 10, TimeUnit.SECONDS);
        redisUtils.expire("key-2", 10, TimeUnit.SECONDS);
        redisUtils.expire("key-3", 10, TimeUnit.SECONDS);
        redisUtils.expire("key-4", 10, TimeUnit.SECONDS);
        print();
    }

    @Test
    void testIncr() {
        System.out.println(redisUtils.incr("key-incr-1", 1));
        System.out.println(redisUtils.incr("key-incr-2", 0));
        System.out.println(redisUtils.incr("key-incr-3", -1));
        System.out.println(redisUtils.decr("key-decr-1", 1));
        System.out.println(redisUtils.decr("key-decr-2", 0));
        System.out.println(redisUtils.decr("key-decr-3", -1));
        System.out.println(redisUtils.getExpire("key-incr-1", TimeUnit.SECONDS));
        System.out.println(redisUtils.getExpire("key-decr-1", TimeUnit.SECONDS));
    }

    @Test
    void testExists() {
        System.out.println(redisUtils.exists("key-1"));
        System.out.println(redisUtils.exists("key-999"));
    }

    @Test
    void testKeys() {
        String pattern = "key-incr-*";
        Set<String> keys = redisUtils.keys(pattern);
        System.out.println(keys);
    }

    @Test
    void testDelete() {
        String pattern = "*";
        Set<String> keys = redisUtils.keys(pattern);
        System.out.println(keys);
        redisUtils.delete(keys);
    }

    @Test
    void MultiSet() {
        Map<String, Object> map = new HashMap<>();
        map.put("key-11", "value-11");
        map.put("key-12", "value-12");
        map.put("key-13", "value-13");
        redisUtils.set(map);
        System.out.println(redisUtils.get("key-11"));
        System.out.println(redisUtils.get("key-12"));
        System.out.println(redisUtils.get("key-13"));
    }

    @Test
    void MultiSetStr() {
        Map<String, String> map = new HashMap<>();
        map.put("key-11", "value-11");
        map.put("key-12", "value-12");
        map.put("key-13", "value-13");
        redisUtils.setStr(map);
        System.out.println(redisUtils.getStr("key-11"));
        System.out.println(redisUtils.getStr("key-12"));
        System.out.println(redisUtils.getStr("key-13"));
    }

    private void print() {
        System.out.println("key-1=" + redisUtils.get("key-1") + ", expire=" + redisUtils.getExpire("key-1", TimeUnit.SECONDS));
        System.out.println("key-2=" + redisUtils.getStr("key-2") + ", expire=" + redisUtils.getExpire("key-2", TimeUnit.SECONDS));
        System.out.println("key-3=" + redisUtils.get("key-3") + ", expire=" + redisUtils.getExpire("key-3", TimeUnit.SECONDS));
        System.out.println("key-4=" + redisUtils.getStr("key-4") + ", expire=" + redisUtils.getExpire("key-4", TimeUnit.SECONDS));
    }
}