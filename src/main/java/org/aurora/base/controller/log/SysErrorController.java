package org.aurora.base.controller.log;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysErrorLog;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/sys/error", "/log/error"})
@RequiresAuthentication
public class SysErrorController extends BaseController<SysErrorLog> {
    @Autowired
    public SysErrorController(SysErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    private final SysErrorLogService errorLogService;

    @Override
    protected String getViewPath() {
        return "/log/error/error_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_error";
    }

    @Override
    protected BaseService<SysErrorLog> getService() {
        return errorLogService;
    }
}
