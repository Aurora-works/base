package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysUserDao;
import org.aurora.base.entity.sys.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserDaoImpl extends BaseDaoImpl<SysUser> implements SysUserDao {

    @Override
    public SysUser findByUsername(String username) {
        String hql = "from SysUser where username = :username";
        return getSession().createSelectionQuery(hql, SysUser.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    public SysUser findByMobile(String mobilePhoneNumber) {
        String hql = "from SysUser where mobilePhoneNumber = :mobilePhoneNumber";
        return getSession().createSelectionQuery(hql, SysUser.class)
                .setParameter("mobilePhoneNumber", mobilePhoneNumber)
                .uniqueResult();
    }
}
