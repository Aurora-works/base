package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.service.BaseService;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public interface SysUserService extends BaseService<SysUser> {

    SysUser findByUsername(String username);

    SysUser findByMobile(String mobilePhoneNumber);

    Map<String, Set<String>> getAuthorizationInfo(Long id);

    TreeSet<SysMenu> getMenuTree(Long id);
}
