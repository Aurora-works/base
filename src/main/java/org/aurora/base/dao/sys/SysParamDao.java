package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysParam;

public interface SysParamDao extends BaseDao<SysParam> {

    SysParam findByCode(String paramCode);
}
