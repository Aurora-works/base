package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysTableServiceImpl extends BaseServiceImpl<SysTable> implements SysTableService {
    @Autowired
    public SysTableServiceImpl(SysTableDao tableDao) {
        this.tableDao = tableDao;
    }

    private final SysTableDao tableDao;

    @Override
    protected BaseDao<SysTable> getDao() {
        return tableDao;
    }
}
