package org.aurora.base.service.impl.sys;

import org.aurora.base.common.dto.TableFormatter;
import org.aurora.base.common.view.ComboTreeHelper;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysMenuDao;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void update(SysMenu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            throw new IllegalArgumentException();
        }
        menuDao.update(menu);
    }

    @Override
    public List<TableFormatter> getFormatters() {
        List<TableFormatter> formatters = super.getFormatters();
        formatters.addAll(menuDao.getFormatters());
        return formatters;
    }

    @Override
    public List<ComboTreeHelper> getComboTree() {
        List<SysMenu> menus = menuDao.findAllWithCache("orderBy", "asc");
        List<ComboTreeHelper> list = new ArrayList<>();
        menus.forEach(menu -> {
            if (menu.getParentId() == null) {
                list.add(getChildren(menu, menus));
            }
        });
        return list;
    }

    private ComboTreeHelper getChildren(SysMenu menu, List<SysMenu> menus) {
        List<ComboTreeHelper> list = new ArrayList<>();
        ComboTreeHelper comboTree = ComboTreeHelper.builder()
                .id(menu.getId().toString())
                .text(menu.getMenuName())
                .iconCls(menu.getCss())
                .build();
        menus.forEach(item -> {
            if (menu.getId().equals(item.getParentId())) {
                list.add(getChildren(item, menus));
            }
        });
        comboTree.setChildren(list);
        return comboTree;
    }
}
