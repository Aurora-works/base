package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysRoleDao;
import org.aurora.base.entity.sys.SysRole;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysRoleService;
import org.aurora.base.util.view.ComboTreeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void update(SysRole role) {
        if (role.getId().equals(role.getParentId())) {
            throw new IllegalArgumentException();
        }
        super.update(role);
    }

    @Override
    public List<ComboTreeHelper> getComboTree() {
        List<SysRole> roles = roleDao.findAll("orderBy", "asc", null);
        List<ComboTreeHelper> list = new ArrayList<>();
        roles.forEach(role -> {
            if (role.getParentId() == null) {
                list.add(getChildren(role, roles));
            }
        });
        return list;
    }

    private ComboTreeHelper getChildren(SysRole role, List<SysRole> roles) {
        ComboTreeHelper comboTree = ComboTreeHelper.builder()
                .id(role.getId().toString())
                .text(role.getRoleName())
                .iconCls("icon-man")
                .build();
        List<ComboTreeHelper> list = new ArrayList<>();
        roles.forEach(item -> {
            if (role.getId().equals(item.getParentId())) {
                list.add(getChildren(item, roles));
            }
        });
        comboTree.setChildren(list);
        return comboTree;
    }
}
