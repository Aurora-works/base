package org.aurora.base.controller.sys;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysUserService;
import org.aurora.base.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/sys/user")
@RequiresAuthentication
public class SysUserController extends BaseController<SysUser> {
    @Autowired
    public SysUserController(SysUserService userService) {
        this.userService = userService;
    }

    private final SysUserService userService;

    @Override
    protected String getViewPath() {
        return "/sys/user/user_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_user";
    }

    @Override
    protected BaseService<SysUser> getService() {
        return userService;
    }

    /**
     * 重置密码
     */
    @PostMapping(value = "/resetPwd")
    @RequiresPermissions("sys_user:update")
    @ResponseBody
    public Result<Object> resetPwd(@RequestParam(value = "ids[]") Long[] ids) {
        userService.resetPwd(ids);
        return Result.success();
    }
}
