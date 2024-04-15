package org.aurora.base.service.impl.sys;

import org.aurora.base.util.enums.Status;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysRoleMenuDao;
import org.aurora.base.dao.sys.SysUserDao;
import org.aurora.base.dao.sys.SysUserRoleDao;
import org.aurora.base.entity.sys.SysRoleMenu;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.entity.sys.SysUserRole;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {
    @Autowired
    public SysUserServiceImpl(SysUserDao userDao, SysUserRoleDao userRoleDao, SysRoleMenuDao roleMenuDao) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.roleMenuDao = roleMenuDao;
    }

    private final SysUserDao userDao;
    private final SysUserRoleDao userRoleDao;
    private final SysRoleMenuDao roleMenuDao;

    @Override
    protected BaseDao<SysUser> getDao() {
        return userDao;
    }

    @Override
    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public SysUser findByMobile(String mobilePhoneNumber) {
        return userDao.findByMobile(mobilePhoneNumber);
    }

    @Override
    public Map<String, Set<String>> getAuthorizationInfo(Long id) {

        Map<String, Set<String>> resultMap = new HashMap<>();
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        Set<Long> roleIds = new HashSet<>();

        List<SysUserRole> userRoles = userRoleDao.findByUserIdWithFetchGraph(id);
        userRoles.stream()
                .map(SysUserRole::getRole)
                .filter(r -> Status.ENABLED.getKey().equals(r.getStatus()))
                .forEach(r -> {
                    roles.add(r.getRoleCode());
                    roleIds.add(r.getId());
                });
        List<SysRoleMenu> roleMenus = roleMenuDao.findByRoleIdWithFetchGraph(roleIds.toArray(new Long[0]));
        roleMenus.stream()
                .filter(rm -> Status.ENABLED.getKey().equals(rm.getMenu().getStatus()))
                .forEach(rm -> {
                    String menuCode = rm.getMenu().getMenuCode();
                    if (Status.ENABLED.getKey().equals(rm.getCreateOp())) {
                        permissions.add(menuCode + ":create");
                    }
                    if (Status.ENABLED.getKey().equals(rm.getUpdateOp())) {
                        permissions.add(menuCode + ":update");
                    }
                    if (Status.ENABLED.getKey().equals(rm.getDeleteOp())) {
                        permissions.add(menuCode + ":delete");
                    }
                    if (Status.ENABLED.getKey().equals(rm.getReadOp())) {
                        permissions.add(menuCode + ":read");
                    }
                });
        resultMap.put("roles", roles);
        resultMap.put("permissions", permissions);
        return resultMap;
    }
}
