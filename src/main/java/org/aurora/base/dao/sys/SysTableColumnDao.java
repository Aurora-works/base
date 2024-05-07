package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysTableColumn;

import java.util.List;

public interface SysTableColumnDao extends BaseDao<SysTableColumn> {

    List<SysTableColumn> findByTableEntityName(String tableEntityName);
}
