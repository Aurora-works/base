package org.aurora.base.service.sys;

import org.aurora.base.common.dto.PersonalData;
import org.aurora.base.common.view.PageHelper;
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

    void updatePersonalData(PersonalData data);

    void resetPwd(Long[] ids);

    PageHelper<SysUser> findByRoleId(int page, int size, String sort, String order, Long roleId);
}
