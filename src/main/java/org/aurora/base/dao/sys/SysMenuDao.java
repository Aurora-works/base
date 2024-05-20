package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.util.dto.TableFormatter;

import java.util.List;

public interface SysMenuDao extends BaseDao<SysMenu> {

    List<TableFormatter> getFormatters();
}
