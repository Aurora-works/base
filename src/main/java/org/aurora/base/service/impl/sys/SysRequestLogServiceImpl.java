package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysRequestLogDao;
import org.aurora.base.entity.sys.SysRequestLog;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRequestLogServiceImpl extends BaseServiceImpl<SysRequestLog> implements SysRequestLogService {
    @Autowired
    public SysRequestLogServiceImpl(SysRequestLogDao requestLogDao) {
        this.requestLogDao = requestLogDao;
    }

    private final SysRequestLogDao requestLogDao;

    @Override
    protected BaseDao<SysRequestLog> getDao() {
        return requestLogDao;
    }
}
