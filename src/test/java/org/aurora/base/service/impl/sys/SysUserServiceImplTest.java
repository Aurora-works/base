package org.aurora.base.service.impl.sys;

import org.aurora.base.service.sys.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;
import java.util.Set;

@SpringJUnitConfig(locations = {
        "classpath:applicationContext.xml",
        "classpath:spring-redis.xml",
        "classpath:spring-shiro.xml"})
class SysUserServiceImplTest {

    @Autowired
    private SysUserService service;

    @Test
    void getAuthorizationInfo() {
        Map<String, Set<String>> map = service.getAuthorizationInfo(1L);
        System.out.println(map);
    }
}