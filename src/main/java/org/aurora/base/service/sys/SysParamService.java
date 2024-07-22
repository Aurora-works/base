package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysParam;
import org.aurora.base.service.BaseService;

public interface SysParamService extends BaseService<SysParam> {

    String getValueByCode(String code);
}
