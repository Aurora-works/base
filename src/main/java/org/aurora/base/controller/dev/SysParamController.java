package org.aurora.base.controller.dev;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysParam;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/sys/param", "/dev/param"})
@RequiresAuthentication
public class SysParamController extends BaseController<SysParam> {
    @Autowired
    public SysParamController(SysParamService paramService) {
        this.paramService = paramService;
    }

    private final SysParamService paramService;

    @Override
    protected String getViewPath() {
        return "/dev/param/param_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_param";
    }

    @Override
    protected BaseService<SysParam> getService() {
        return paramService;
    }
}
