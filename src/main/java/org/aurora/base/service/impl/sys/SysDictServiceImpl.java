package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysDictDao;
import org.aurora.base.entity.sys.SysDict;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysDictService;
import org.aurora.base.util.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDict> implements SysDictService {
    @Autowired
    public SysDictServiceImpl(SysDictDao dictDao) {
        this.dictDao = dictDao;
    }

    private final SysDictDao dictDao;

    @Override
    protected BaseDao<SysDict> getDao() {
        return dictDao;
    }

    @Override
    public List<SysDict> findByCode(String dictCode) {
        return dictDao.findByCode(dictCode).stream()
                .filter(dict -> Status.ENABLED.getKey().equals(dict.getStatus()))
                .toList();
    }
}
