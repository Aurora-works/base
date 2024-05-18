package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysRole;
import org.aurora.base.service.BaseService;
import org.aurora.base.util.view.ComboTreeHelper;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {

    List<ComboTreeHelper> getComboTree();
}
