package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysErrorLogDao;
import org.aurora.base.entity.sys.SysErrorLog;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysErrorLogServiceImpl extends BaseServiceImpl<SysErrorLog> implements SysErrorLogService {
    @Autowired
    public SysErrorLogServiceImpl(SysErrorLogDao errorLogDao) {
        this.errorLogDao = errorLogDao;
    }

    private final SysErrorLogDao errorLogDao;

    @Override
    protected BaseDao<SysErrorLog> getDao() {
        return errorLogDao;
    }

    @Override
    public void newErrorLog(SysErrorLog sysErrorLog) {
        getDao().create(sysErrorLog);
    }
}
