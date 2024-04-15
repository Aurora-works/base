package org.aurora.base.service.impl;

import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.service.sys.SysTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class BaseServiceImplTest {

    @Autowired
    private SysTableService tableService;

    @Test
    void findById() {
        SysTable table = tableService.findById(701L);
        System.out.println(table);
        System.out.println(table.getCreateUser());
        System.out.println(table.getLastUser());
    }
}