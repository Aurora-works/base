package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuDao extends BaseDao<SysRoleMenu> {

    List<SysRoleMenu> findByRoleIdWithFetchGraph(Long[] roleIds);
}
