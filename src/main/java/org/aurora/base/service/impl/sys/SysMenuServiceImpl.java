package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysMenuDao;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {
    @Autowired
    public SysMenuServiceImpl(SysMenuDao menuDao) {
        this.menuDao = menuDao;
    }

    private final SysMenuDao menuDao;

    @Override
    protected BaseDao<SysMenu> getDao() {
        return menuDao;
    }
}
