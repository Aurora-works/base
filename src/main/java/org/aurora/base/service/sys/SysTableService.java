package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.service.BaseService;

import java.util.List;
import java.util.Map;

public interface SysTableService extends BaseService<SysTable> {

    void save(Map<String, List<SysTable>> changes);
}
