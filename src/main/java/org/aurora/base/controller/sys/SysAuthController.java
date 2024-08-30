package org.aurora.base.controller.sys;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.common.Result;
import org.aurora.base.common.dto.SysAuthMenu;
import org.aurora.base.common.view.PageHelper;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysRoleMenu;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/auth")
@RequiresAuthentication
public class SysAuthController extends BaseController<SysRoleMenu> {
    @Autowired
    public SysAuthController(SysRoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    private final SysRoleMenuService roleMenuService;

    @Override
    protected String getViewPath() {
        return "/sys/auth/auth_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_auth";
    }

    @Override
    protected BaseService<SysRoleMenu> getService() {
        return roleMenuService;
    }

    /**
     * 功能菜单列表
     */
    @PostMapping(value = "/menu/list")
    @RequiresPermissions("sys_auth:read")
    @ResponseBody
    public PageHelper<SysAuthMenu> findByRoleId(
            @RequestParam(value = "sort", required = false, defaultValue = "orderBy") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "rid") Long roleId) {
        return roleMenuService.findByRoleId(sort, order, roleId);
    }

    /**
     * 保存修改
     */
    @PostMapping(value = "/save")
    @RequiresPermissions("sys_auth:update")
    @ResponseBody
    public Result<Object> save(
            @RequestParam(value = "rid") Long roleId,
            @RequestBody List<SysAuthMenu> changes) {
        roleMenuService.save(roleId, changes);
        return Result.success();
    }
}
