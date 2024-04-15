package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysOperationLog;
import org.aurora.base.service.BaseService;

public interface SysOperationLogService extends BaseService<SysOperationLog> {

    void newOperationLog(SysOperationLog operationLog);
}
