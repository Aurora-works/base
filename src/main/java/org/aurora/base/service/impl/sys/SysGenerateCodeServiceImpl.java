package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysGenerateCodeDao;
import org.aurora.base.entity.sys.SysGenerateCode;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysGenerateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysGenerateCodeServiceImpl extends BaseServiceImpl<SysGenerateCode> implements SysGenerateCodeService {
    @Autowired
    public SysGenerateCodeServiceImpl(SysGenerateCodeDao generateCodeDao) {
        this.generateCodeDao = generateCodeDao;
    }

    private final SysGenerateCodeDao generateCodeDao;

    @Override
    protected BaseDao<SysGenerateCode> getDao() {
        return generateCodeDao;
    }
}
