package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysUserRoleDao;
import org.aurora.base.entity.sys.SysUserRole;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRole> implements SysUserRoleService {
    @Autowired
    public SysUserRoleServiceImpl(SysUserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    private final SysUserRoleDao userRoleDao;

    @Override
    protected BaseDao<SysUserRole> getDao() {
        return userRoleDao;
    }
}
