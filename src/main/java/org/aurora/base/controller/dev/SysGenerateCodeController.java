package org.aurora.base.controller.dev;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysGenerateCode;
import org.aurora.base.exception.BusinessException;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysGenerateCodeService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 保存修改
     */
    @PostMapping(value = "/save")
    @RequiresPermissions(value = {"sys_generate:create", "sys_generate:update", "sys_generate:delete"}, logical = Logical.OR)
    @ResponseBody
    public Result<Object> save(@RequestBody Map<String, List<SysGenerateCode>> changes) {
        if (!ShiroUtils.isPermitted("sys_generate:create")) {
            changes.put("inserted", null);
        }
        if (!ShiroUtils.isPermitted("sys_generate:update")) {
            changes.put("updated", null);
        }
        if (!ShiroUtils.isPermitted("sys_generate:delete")) {
            changes.put("deleted", null);
        }
        generateCodeService.save(changes);
        return Result.success();
    }

    /**
     * 生成代码
     */
    @PostMapping(value = "/code")
    @RequiresPermissions("sys_generate:read")
    @ResponseBody
    public Result<Object> code(@RequestParam Long id) {
        String OSName = System.getProperty("os.name");
        if (!OSName.startsWith("Windows")) {
            throw new BusinessException("暂不支持 [" + OSName + "] 系统, 此功能仅支持Windows本地开发时使用");
        }
        generateCodeService.code(id);
        return Result.success();
    }
}
