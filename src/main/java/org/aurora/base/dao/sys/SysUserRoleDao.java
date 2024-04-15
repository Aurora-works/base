package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysUserRole;

import java.util.List;

public interface SysUserRoleDao extends BaseDao<SysUserRole> {

    List<SysUserRole> findByUserIdWithFetchGraph(Long userId);

    void delete(Long roleId, Long[] userIds);
}
