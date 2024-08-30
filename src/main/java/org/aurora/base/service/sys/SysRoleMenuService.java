package org.aurora.base.service.sys;

import org.aurora.base.common.dto.SysAuthMenu;
import org.aurora.base.common.view.PageHelper;
import org.aurora.base.entity.sys.SysRoleMenu;
import org.aurora.base.service.BaseService;

import java.util.List;

public interface SysRoleMenuService extends BaseService<SysRoleMenu> {

    PageHelper<SysAuthMenu> findByRoleId(String sort, String order, Long roleId);

    void save(Long roleId, List<SysAuthMenu> authMenus);
}
