package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysDict;
import org.aurora.base.service.BaseService;

import java.util.List;
import java.util.Map;

public interface SysDictService extends BaseService<SysDict> {

    List<SysDict> findByCode(String dictCode);

    void save(Map<String, List<SysDict>> changes);
}
