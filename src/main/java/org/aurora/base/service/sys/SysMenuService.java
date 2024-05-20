package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.service.BaseService;
import org.aurora.base.util.view.ComboTreeHelper;

import java.util.List;

public interface SysMenuService extends BaseService<SysMenu> {

    List<ComboTreeHelper> getComboTree();
}
