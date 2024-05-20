package org.aurora.base.controller.sys;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysMenuService;
import org.aurora.base.util.view.ComboTreeHelper;
import org.aurora.base.util.view.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/menu")
@RequiresAuthentication
public class SysMenuController extends BaseController<SysMenu> {
    @Autowired
    public SysMenuController(SysMenuService menuService) {
        this.menuService = menuService;
    }

    private final SysMenuService menuService;

    @Override
    protected String getViewPath() {
        return "/sys/menu/menu_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_menu";
    }

    @Override
    protected BaseService<SysMenu> getService() {
        return menuService;
    }

    /**
     * 数据列表
     */
    @PostMapping(value = "/tree")
    @RequiresPermissions("sys_menu:read")
    @ResponseBody
    public PageHelper<SysMenu> getTree(
            @RequestParam(value = "sort", required = false, defaultValue = "orderBy") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
        return menuService.findAllInPage(sort, order);
    }

    /**
     * 获取树形下拉菜单数据列表
     */
    @PostMapping(value = "/tree/combo")
    @ResponseBody
    public List<ComboTreeHelper> getComboTree() {
        return menuService.getComboTree();
    }

    /**
     * 根据id获取数据
     */
    @GetMapping(value = "/find")
    @RequiresPermissions("sys_auth:read")
    @ResponseBody
    public SysMenu findById(@RequestParam Long id) {
        return menuService.findById(id);
    }
}
