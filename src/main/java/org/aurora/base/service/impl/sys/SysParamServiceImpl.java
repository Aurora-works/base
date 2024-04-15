package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysParamDao;
import org.aurora.base.entity.sys.SysParam;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysParamServiceImpl extends BaseServiceImpl<SysParam> implements SysParamService {
    @Autowired
    public SysParamServiceImpl(SysParamDao paramDao) {
        this.paramDao = paramDao;
    }

    private final SysParamDao paramDao;

    @Override
    protected BaseDao<SysParam> getDao() {
        return paramDao;
    }
}
