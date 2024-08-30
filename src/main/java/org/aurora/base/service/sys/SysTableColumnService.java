package org.aurora.base.service.sys;

import org.aurora.base.common.view.PageHelper;
import org.aurora.base.entity.sys.SysTableColumn;
import org.aurora.base.service.BaseService;

import java.util.List;
import java.util.Map;

public interface SysTableColumnService extends BaseService<SysTableColumn> {

    void save(Map<String, List<SysTableColumn>> changes);

    PageHelper<SysTableColumn> findByTableId(int page, int size, String sort, String order, Long tableId);
}
