package org.aurora.base.service.sys;

import org.aurora.base.entity.sys.SysGenerateCode;
import org.aurora.base.service.BaseService;

import java.util.List;
import java.util.Map;

public interface SysGenerateCodeService extends BaseService<SysGenerateCode> {

    void save(Map<String, List<SysGenerateCode>> changes);

    void code(Long id);
}
