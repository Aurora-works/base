package org.aurora.base.service.impl.sys;

import org.aurora.base.common.view.ComboTreeHelper;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysRoleDao;
import org.aurora.base.entity.sys.SysRole;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysRoleService;
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
        roleDao.update(role);
    }

    @Override
    public List<ComboTreeHelper> getComboTree() {
        List<SysRole> roles = roleDao.findAllWithCache("orderBy", "asc");
        List<ComboTreeHelper> list = new ArrayList<>();
        roles.forEach(role -> {
            if (role.getParentId() == null) {
                list.add(getChildren(role, roles));
            }
        });
        return list;
    }

    private ComboTreeHelper getChildren(SysRole role, List<SysRole> roles) {
        List<ComboTreeHelper> list = new ArrayList<>();
        ComboTreeHelper comboTree = ComboTreeHelper.builder()
                .id(role.getId().toString())
                .text(role.getRoleName())
                .iconCls("icon-man")
                .build();
        roles.forEach(item -> {
            if (role.getId().equals(item.getParentId())) {
                list.add(getChildren(item, roles));
            }
        });
        comboTree.setChildren(list);
        return comboTree;
    }
}
