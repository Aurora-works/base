package org.aurora.base.service.sys;

import org.aurora.base.common.view.ComboTreeHelper;
import org.aurora.base.entity.sys.SysRole;
import org.aurora.base.service.BaseService;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {

    List<ComboTreeHelper> getComboTree();
}
