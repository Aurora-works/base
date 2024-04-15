package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysRoleMenuDao;
import org.aurora.base.entity.sys.SysRoleMenu;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenu> implements SysRoleMenuService {
    @Autowired
    public SysRoleMenuServiceImpl(SysRoleMenuDao roleMenuDao) {
        this.roleMenuDao = roleMenuDao;
    }

    private final SysRoleMenuDao roleMenuDao;

    @Override
    protected BaseDao<SysRoleMenu> getDao() {
        return roleMenuDao;
    }
}
