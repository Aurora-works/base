package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysUserRoleDao;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.entity.sys.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysUserRoleDaoImpl extends BaseDaoImpl<SysUserRole> implements SysUserRoleDao {

    @Override
    public List<SysUserRole> findByUserIdWithFetchGraph(Long userId) {
        String hql = "from SysUserRole ur join fetch ur.role where ur.userId = :userId";
        return getSession().createSelectionQuery(hql, SysUserRole.class)
                .setParameter("userId", userId)
                .setCacheable(true)
                .setCacheRegion("AuthorizationInfoRegion")
                .list();
    }

    @Override
    public void delete(Long roleId, Long[] userIds) {
        String hql = "delete from SysUserRole where roleId = :roleId and userId in(:userIds)";
        getSession().createMutationQuery(hql)
                .setParameter("roleId", roleId)
                .setParameterList("userIds", userIds)
                .executeUpdate();
    }

    @Override
    public void deleteByUserIds(Long[] userIds) {
        String hql = "delete from SysUserRole where userId in(:userIds)";
        getSession().createMutationQuery(hql)
                .setParameterList("userIds", userIds)
                .executeUpdate();
    }

    @Override
    public List<SysUserRole> findByRoleIdAndUserIds(Long roleId, Long[] userIds) {
        String hql = "from SysUserRole where roleId = :roleId and userId in(:userIds)";
        return getSession().createSelectionQuery(hql, SysUserRole.class)
                .setParameter("roleId", roleId)
                .setParameterList("userIds", userIds)
                .list();
    }

    @Override
    public List<SysUser> findUserListByRoleId(int page, int size, String sort, String order, Long roleId) {
        String hql = "from SysUser u join u.userRoles ur where ur.roleId = :roleId order by u." + sort + " " + order;
        return getSession().createSelectionQuery(hql, SysUser.class)
                .setParameter("roleId", roleId)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .list();
    }

    @Override
    public long getTotalByRoleId(Long roleId) {
        String hql = "select count(*) from SysUserRole where roleId = :roleId";
        return getSession().createSelectionQuery(hql, Long.class)
                .setParameter("roleId", roleId)
                .uniqueResult();
    }
}
