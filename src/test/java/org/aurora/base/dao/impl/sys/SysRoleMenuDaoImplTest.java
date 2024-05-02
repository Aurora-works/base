package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.sys.SysRoleMenuDao;
import org.aurora.base.entity.sys.SysRoleMenu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class SysRoleMenuDaoImplTest {

    @Autowired
    SysRoleMenuDao dao;

    @Test
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    void findByRoleIdWithFetchGraph() {
        List<SysRoleMenu> roleMenus = dao.findByRoleIdWithFetchGraph(new Long[]{101L});
        roleMenus.forEach(System.out::println);
    }
}