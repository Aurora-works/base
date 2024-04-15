package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysRoleDao;
import org.aurora.base.entity.sys.SysRole;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements SysRoleService {
    @Autowired
    public SysRoleServiceImpl(SysRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    private final SysRoleDao roleDao;

    @Override
    protected BaseDao<SysRole> getDao() {
        return roleDao;
    }
}
