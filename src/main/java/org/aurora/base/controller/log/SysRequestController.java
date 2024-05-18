package org.aurora.base.controller.log;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysRequestLog;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/sys/request", "/log/request"})
@RequiresAuthentication
public class SysRequestController extends BaseController<SysRequestLog> {
    @Autowired
    public SysRequestController(SysRequestLogService logService) {
        this.logService = logService;
    }

    private final SysRequestLogService logService;

    @Override
    protected String getViewPath() {
        return "/log/request/request_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_request";
    }

    @Override
    protected BaseService<SysRequestLog> getService() {
        return logService;
    }
}
