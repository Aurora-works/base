package org.aurora.base.service.impl;

import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.service.sys.SysRoleMenuService;
import org.aurora.base.service.sys.SysTableService;
import org.aurora.base.util.dto.TableFormatter;
import org.aurora.base.util.view.FilterRuleHelper;
import org.aurora.base.util.view.PageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class BaseServiceImplTest {

    @Autowired
    private SysTableService tableService;
    @Autowired
    private SysRoleMenuService roleMenuService;

    @Test
    void findById() {
        SysTable table = tableService.findById(701L);
        System.out.println(table);
        System.out.println(table.getCreateUser());
        System.out.println(table.getLastUser());
    }

    @Test
    void findAll() {
        FilterRuleHelper filterRule = FilterRuleHelper.builder()
                .field("createUser.nickname")
                .op(FilterRuleHelper.CONTAINS)
                .value("管理员")
                .build();
        List<FilterRuleHelper> filterRules = new ArrayList<>();
        filterRules.add(filterRule);
        PageHelper<SysTable> tables = tableService.findAll(1, 50, "id", "desc", filterRules);
        tables.getRows().forEach(System.out::println);
        System.out.println(tables.getTotal());
    }

    @Test
    void getFormatters() {
        List<TableFormatter> formatters = roleMenuService.getFormatters();
        formatters.forEach(System.out::println);
    }
}