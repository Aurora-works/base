package org.aurora.base.controller.dev;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysGenerateCode;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysGenerateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/sys/generate", "/dev/generate"})
@RequiresAuthentication
public class SysGenerateCodeController extends BaseController<SysGenerateCode> {
    @Autowired
    public SysGenerateCodeController(SysGenerateCodeService generateCodeService) {
        this.generateCodeService = generateCodeService;
    }

    private final SysGenerateCodeService generateCodeService;

    @Override
    protected String getViewPath() {
        return "/dev/generate/generate_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_generate";
    }

    @Override
    protected BaseService<SysGenerateCode> getService() {
        return generateCodeService;
    }
}
