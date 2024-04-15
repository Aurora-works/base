package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysTableColumnDao;
import org.aurora.base.entity.sys.SysTableColumn;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysTableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysTableColumnServiceImpl extends BaseServiceImpl<SysTableColumn> implements SysTableColumnService {
    @Autowired
    public SysTableColumnServiceImpl(SysTableColumnDao columnDao) {
        this.columnDao = columnDao;
    }

    private final SysTableColumnDao columnDao;

    @Override
    protected BaseDao<SysTableColumn> getDao() {
        return columnDao;
    }
}
