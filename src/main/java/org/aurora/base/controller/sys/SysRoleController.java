package org.aurora.base.controller.sys;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.common.Result;
import org.aurora.base.common.view.ComboTreeHelper;
import org.aurora.base.common.view.PageHelper;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysRole;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysRoleService;
import org.aurora.base.service.sys.SysUserRoleService;
import org.aurora.base.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/sys/role")
@RequiresAuthentication
public class SysRoleController extends BaseController<SysRole> {
    @Autowired
    public SysRoleController(SysUserService userService, SysRoleService roleService, SysUserRoleService userRoleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysUserRoleService userRoleService;

    @Override
    protected String getViewPath() {
        return "/sys/role/role_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_role";
    }

    @Override
    protected BaseService<SysRole> getService() {
        return roleService;
    }

    /**
     * 系统角色树
     */
    @PostMapping(value = "/tree")
    @RequiresPermissions(value = {"sys_role:read", "sys_auth:read"}, logical = Logical.OR)
    @ResponseBody
    public List<SysRole> getTree() {
        return roleService.findAll("orderBy", "asc");
    }

    /**
     * 获取树形下拉菜单数据列表
     */
    @PostMapping(value = "/tree/combo")
    @ResponseBody
    public List<ComboTreeHelper> getComboTree() {
        return roleService.getComboTree();
    }

    /**
     * 关联用户列表
     */
    @PostMapping(value = "/user/list")
    @RequiresPermissions("sys_role:read")
    @ResponseBody
    public PageHelper<SysUser> getRoleUserList(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "50") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "rid") Long roleId) {
        return userService.findByRoleId(page, size, sort, order, roleId);
    }

    /**
     * 添加关联用户
     */
    @PostMapping(value = "/user/create")
    @RequiresPermissions("sys_role:update")
    @ResponseBody
    public Result<Object> createRoleUsers(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "userIds[]") Long[] userIds) {
        userRoleService.create(roleId, userIds);
        return Result.success();
    }

    /**
     * 解除关联用户
     */
    @PostMapping(value = "/user/delete")
    @RequiresPermissions("sys_role:update")
    @ResponseBody
    public Result<Object> deleteRoleUsers(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "userIds[]") Long[] userIds) {
        userRoleService.delete(roleId, userIds);
        return Result.success();
    }
}
