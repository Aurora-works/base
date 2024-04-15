package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysOperationLogDao;
import org.aurora.base.entity.sys.SysOperationLog;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysOperationLogServiceImpl extends BaseServiceImpl<SysOperationLog> implements SysOperationLogService {
    @Autowired
    public SysOperationLogServiceImpl(SysOperationLogDao operationLogDao) {
        this.operationLogDao = operationLogDao;
    }

    private final SysOperationLogDao operationLogDao;

    @Override
    protected BaseDao<SysOperationLog> getDao() {
        return operationLogDao;
    }

    @Override
    public void newOperationLog(SysOperationLog operationLog) {
        getDao().create(operationLog);
    }
}
