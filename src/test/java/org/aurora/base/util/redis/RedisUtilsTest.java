package org.aurora.base.util.redis;

import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.extension.TimingExtension;
import org.aurora.base.service.sys.SysTableService;
import org.aurora.base.service.sys.SysUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private SysUserService userService;

    @Test
    void testUserObj() {
        SysUser user = userService.findById(1L);
        System.out.println(user);
        redisUtils.set("key-user", user);
        System.out.println(redisUtils.getStr("key-user"));
        SysUser result = (SysUser) redisUtils.get("key-user");
        System.out.println(result);
    }

    @Test
    void testTableObj() {
        SysTable table = tableService.findById(701L);
        System.out.println(table);
        redisUtils.set("key-table", table);
        System.out.println(redisUtils.getStr("key-table"));
        SysTable result = (SysTable) redisUtils.get("key-table");
        System.out.println(result);
        System.out.println(result.getCreateUser());
        System.out.println(result.getLastUser());
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
    void multiSet() {
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
    void multiGet() {
        Set<String> keys = Set.of("key-11", "key-12", "key-13");
        List<Object> list = redisUtils.get(keys);
        list.forEach(System.out::println);
    }

    @Test
    void multiSetStr() {
        Map<String, String> map = new HashMap<>();
        map.put("key-14", "value-14");
        map.put("key-15", "value-15");
        map.put("key-16", "value-16");
        redisUtils.setStr(map);
        System.out.println(redisUtils.getStr("key-14"));
        System.out.println(redisUtils.getStr("key-15"));
        System.out.println(redisUtils.getStr("key-16"));
    }

    @Test
    void multiGetStr() {
        Set<String> keys = Set.of("key-14", "key-15", "key-16");
        List<String> list = redisUtils.getStr(keys);
        list.forEach(System.out::println);
    }

    private void print() {
        System.out.println("key-1=" + redisUtils.get("key-1") + ", expire=" + redisUtils.getExpire("key-1", TimeUnit.SECONDS));
        System.out.println("key-2=" + redisUtils.getStr("key-2") + ", expire=" + redisUtils.getExpire("key-2", TimeUnit.SECONDS));
        System.out.println("key-3=" + redisUtils.get("key-3") + ", expire=" + redisUtils.getExpire("key-3", TimeUnit.SECONDS));
        System.out.println("key-4=" + redisUtils.getStr("key-4") + ", expire=" + redisUtils.getExpire("key-4", TimeUnit.SECONDS));
    }
}