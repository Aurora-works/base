package org.aurora.base.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.service.sys.SysParamService;
import org.aurora.base.service.sys.SysUserService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.jackson.JSONUtils;
import org.aurora.base.util.enums.SysParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.TreeSet;

@Controller
@RequiresAuthentication
public class IndexController {
    @Autowired
    public IndexController(SysUserService userService, SysParamService paramService) {
        this.userService = userService;
        this.paramService = paramService;
    }

    private final SysUserService userService;
    private final SysParamService paramService;

    /**
     * 主页
     */
    @GetMapping(value = "/")
    public String index(Model model) {
        Long currentUserId = ShiroUtils.getCurrentUserId();
        TreeSet<SysMenu> menus = userService.getMenuTree(currentUserId);
        model.addAttribute("westMenuData", JSONUtils.writeValueAsString(menus));
        String defaultThemes = paramService.getValueByCode(SysParam.SYS_DEFAULT_THEMES.getCode());
        model.addAttribute("defaultThemes", defaultThemes);
        return "index";
    }
}
