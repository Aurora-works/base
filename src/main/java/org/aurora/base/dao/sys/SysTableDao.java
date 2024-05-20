package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.util.dto.TableFormatter;

import java.util.List;

public interface SysTableDao extends BaseDao<SysTable> {

    // TODO
    List<TableFormatter> findFormatterByIds(Long[] ids);

    SysTable findByEntityName(String entityName);
}
