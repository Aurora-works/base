package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.sys.SysUserRoleDao;
import org.aurora.base.entity.sys.SysUserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringJUnitConfig(locations = {
        "classpath:applicationContext.xml",
        "classpath:spring-redis.xml",
        "classpath:spring-shiro.xml"})
class SysUserRoleDaoImplTest {

    @Autowired
    SysUserRoleDao dao;

    @Test
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    void findByUserIdWithFetchGraph() {
        List<SysUserRole> userRoles = dao.findByUserIdWithFetchGraph(1L);
        userRoles.forEach(System.out::println);
        userRoles.forEach(ur -> System.out.println(ur.getRole()));
    }
}