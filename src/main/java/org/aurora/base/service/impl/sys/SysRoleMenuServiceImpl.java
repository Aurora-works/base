package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysMenuDao;
import org.aurora.base.dao.sys.SysRoleMenuDao;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.entity.sys.SysRoleMenu;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysRoleMenuService;
import org.aurora.base.util.dto.SysAuthMenu;
import org.aurora.base.util.enums.Status;
import org.aurora.base.util.reflect.FieldUtils;
import org.aurora.base.util.view.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenu> implements SysRoleMenuService {
    @Autowired
    public SysRoleMenuServiceImpl(SysRoleMenuDao roleMenuDao, SysMenuDao menuDao) {
        this.roleMenuDao = roleMenuDao;
        this.menuDao = menuDao;
    }

    private final SysRoleMenuDao roleMenuDao;
    private final SysMenuDao menuDao;

    @Override
    protected BaseDao<SysRoleMenu> getDao() {
        return roleMenuDao;
    }

    @Override
    public PageHelper<SysAuthMenu> findByRoleId(String sort, String order, Long roleId) {
        String defaultSort = "orderBy";
        String defaultOrder = "asc";

        List<SysAuthMenu> authMenus = new ArrayList<>();
        List<SysMenu> menus = menuDao.findAllWithCache(defaultSort, defaultOrder);
        List<SysRoleMenu> roleMenus = roleMenuDao.findByRoleId(roleId);

        Set<Long> parentMenuIds = new HashSet<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId() != null) {
                parentMenuIds.add(menu.getParentId());
            }
        }

        String readOp;
        String createOp;
        String updateOp;
        String deleteOp;
        for (SysMenu menu : menus) {
            readOp = Status.DISABLED.getKey();
            createOp = Status.DISABLED.getKey();
            updateOp = Status.DISABLED.getKey();
            deleteOp = Status.DISABLED.getKey();
            for (SysRoleMenu roleMenu : roleMenus) {
                if (menu.getId().equals(roleMenu.getMenuId())) {
                    readOp = roleMenu.getReadOp();
                    createOp = roleMenu.getCreateOp();
                    updateOp = roleMenu.getUpdateOp();
                    deleteOp = roleMenu.getDeleteOp();
                    break;
                }
            }
            SysAuthMenu authMenu = new SysAuthMenu(
                    menu.getId(),
                    menu.getMenuName(),
                    menu.getParentId(),
                    menu.getOrderBy(),
                    menu.getCss(),
                    readOp,
                    createOp,
                    updateOp,
                    deleteOp,
                    parentMenuIds.contains(menu.getId()));
            authMenus.add(authMenu);
        }

        if (!defaultSort.equals(sort) || !defaultOrder.equals(order)) {
            if ("asc".equals(order)) {
                authMenus.sort(Comparator.comparing(o -> String.valueOf(FieldUtils.readDeclaredField(o, sort))));
            } else {
                authMenus.sort(((o1, o2) -> String.valueOf(FieldUtils.readDeclaredField(o2, sort)).compareTo(String.valueOf(FieldUtils.readDeclaredField(o1, sort)))));
            }
        }

        return new PageHelper<>(authMenus.size(), authMenus, getFormatters());
    }

    @Override
    public void save(Long roleId, List<SysAuthMenu> authMenus) {
        List<SysRoleMenu> roleMenus = roleMenuDao.findByRoleId(roleId);
        outer:
        for (SysAuthMenu authMenu : authMenus) {
            for (SysRoleMenu roleMenu : roleMenus) {
                if (roleMenu.getMenuId().equals(authMenu.id())) {
                    // update
                    roleMenu.setReadOp(authMenu.readOp());
                    roleMenu.setCreateOp(authMenu.createOp());
                    roleMenu.setUpdateOp(authMenu.updateOp());
                    roleMenu.setDeleteOp(authMenu.deleteOp());
                    roleMenuDao.update(roleMenu);
                    continue outer;
                }
            }
            // create
            SysRoleMenu createRoleMenu = new SysRoleMenu();
            createRoleMenu.setRoleId(roleId);
            createRoleMenu.setMenuId(authMenu.id());
            createRoleMenu.setReadOp(authMenu.readOp());
            createRoleMenu.setCreateOp(authMenu.createOp());
            createRoleMenu.setUpdateOp(authMenu.updateOp());
            createRoleMenu.setDeleteOp(authMenu.deleteOp());
            roleMenuDao.create(createRoleMenu);
        }
    }
}
