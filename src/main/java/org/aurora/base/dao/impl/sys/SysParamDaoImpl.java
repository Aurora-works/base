package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysParamDao;
import org.aurora.base.entity.sys.SysParam;
import org.springframework.stereotype.Repository;

@Repository
public class SysParamDaoImpl extends BaseDaoImpl<SysParam> implements SysParamDao {

    @Override
    public SysParam findByCode(String paramCode) {
        String hql = "from SysParam where paramCode = :paramCode";
        return getSession().createSelectionQuery(hql, SysParam.class)
                .setParameter("paramCode", paramCode)
                .uniqueResult();
    }
}
