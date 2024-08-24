package org.aurora.base.dao.impl;

import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.sys.SysTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig(locations = {
        "classpath:applicationContext.xml",
        "classpath:spring-redis.xml",
        "classpath:spring-shiro.xml"})
class BaseDaoImplTest {

    @Autowired
    private SysTableDao tableDao;

    @Test
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    void findById() {
        SysTable table = tableDao.findById(701L);
        System.out.println(table);
        System.out.println(table.getCreateUser());
        System.out.println(table.getLastUser());
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    void findByIdWithFetchGraph() {
        SysTable table = tableDao.findByIdWithFetchGraph(701L);
        System.out.println(table);
        System.out.println(table.getCreateUser());
        System.out.println(table.getLastUser());
    }

    @Test
    @Transactional
    @Rollback(false)
    void delete() {
        tableDao.delete(710L);
    }
}