package org.aurora.base.dao.impl.sys;

import org.aurora.base.util.enums.YesOrNo;
import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysUserDao;
import org.aurora.base.entity.sys.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserDaoImpl extends BaseDaoImpl<SysUser> implements SysUserDao {

    @Override
    public SysUser findByUsername(String username) {
        String hql = "from SysUser where username = :username and isDeleted = :isDeleted";
        return getSession().createSelectionQuery(hql, SysUser.class)
                .setParameter("username", username)
                .setParameter("isDeleted", YesOrNo.NO.getKey())
                .getSingleResult();
    }

    @Override
    public SysUser findByMobile(String mobilePhoneNumber) {
        String hql = "from SysUser where mobilePhoneNumber = :mobilePhoneNumber and isDeleted = :isDeleted";
        return getSession().createSelectionQuery(hql, SysUser.class)
                .setParameter("mobilePhoneNumber", mobilePhoneNumber)
                .setParameter("isDeleted", YesOrNo.NO.getKey())
                .getSingleResult();
    }
}
