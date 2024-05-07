package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysUserDao;
import org.aurora.base.dao.sys.SysUserRoleDao;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.util.enums.YesOrNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserDaoImpl extends BaseDaoImpl<SysUser> implements SysUserDao {
    @Autowired
    public SysUserDaoImpl(SysUserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    private final SysUserRoleDao userRoleDao;

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

    @Override
    public void softDelete(Long[] ids) {
        userRoleDao.deleteByUserIds(ids);
        String hql = "update SysUser set isDeleted = :isDeleted where id in(:ids)";
        getSession().createMutationQuery(hql)
                .setParameter("isDeleted", YesOrNo.YES.getKey())
                .setParameterList("ids", ids)
                .executeUpdate();
    }
}
