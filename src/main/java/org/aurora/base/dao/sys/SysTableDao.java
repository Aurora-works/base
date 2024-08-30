package org.aurora.base.dao.sys;

import org.aurora.base.common.dto.TableFormatter;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysTable;

import java.util.List;

public interface SysTableDao extends BaseDao<SysTable> {

    List<TableFormatter> getFormatters();

    List<SysTable> findByForeignTableId(Long tableId);

    SysTable findByEntityName(String entityName);
}
