package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysRoleMenuDao;
import org.aurora.base.entity.sys.SysRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleMenuDaoImpl extends BaseDaoImpl<SysRoleMenu> implements SysRoleMenuDao {

    @Override
    public List<SysRoleMenu> findByRoleIdWithFetchGraph(Long[] roleIds) {
        String hql = "from SysRoleMenu rm join fetch rm.menu m join fetch m.parentMenu where rm.roleId in(:roleIds)";
        return getSession().createSelectionQuery(hql, SysRoleMenu.class)
                .setParameterList("roleIds", roleIds)
                .list();
    }
}
