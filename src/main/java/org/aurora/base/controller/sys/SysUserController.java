package org.aurora.base.controller.sys;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.common.Result;
import org.aurora.base.common.view.FilterRuleHelper;
import org.aurora.base.common.view.PageHelper;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    /**
     * 窗口数据列表
     */
    @PostMapping(value = "/win")
    @RequiresPermissions("sys_role:update")
    @ResponseBody
    public PageHelper<SysUser> getWin(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "50") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "filterRules", required = false) String filterRules) {
        List<FilterRuleHelper> filterRuleList = parseFilterRules(filterRules);
        return userService.findAllInPage(page, size, sort, order, filterRuleList);
    }
}
