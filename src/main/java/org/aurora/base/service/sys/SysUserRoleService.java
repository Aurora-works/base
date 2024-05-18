package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysUserRole;
import org.aurora.base.service.BaseService;

public interface SysUserRoleService extends BaseService<SysUserRole> {

    void create(Long roleId, Long[] userIds);

    void delete(Long roleId, Long[] userIds);
}
