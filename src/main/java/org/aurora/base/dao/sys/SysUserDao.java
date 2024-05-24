package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysUser;

public interface SysUserDao extends BaseDao<SysUser> {

    SysUser findByUsername(String username);

    SysUser findByMobile(String mobilePhoneNumber);
}
