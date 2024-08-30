package org.aurora.base.dao.sys;

import org.aurora.base.common.dto.TableFormatter;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysDict;

import java.util.List;

public interface SysDictDao extends BaseDao<SysDict> {

    List<TableFormatter> findAllGroupByCode();

    List<TableFormatter> findFormatterByCodes(String[] codes);

    List<SysDict> findByCode(String dictCode);

    List<SysDict> findByCodes(String[] dictCodes);
}
