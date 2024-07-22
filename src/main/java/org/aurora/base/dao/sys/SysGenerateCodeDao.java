package org.aurora.base.dao.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.sys.SysGenerateCode;

public interface SysGenerateCodeDao extends BaseDao<SysGenerateCode> {

    SysGenerateCode findByIdWithFetchGraph2(Long id);
}
