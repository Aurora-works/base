package org.aurora.base.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.service.sys.SysUserService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.TreeSet;

@Controller
@RequiresAuthentication
public class IndexController {
    @Autowired
    public IndexController(SysUserService userService) {
        this.userService = userService;
    }

    private final SysUserService userService;

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/menu")
    @ResponseBody
    public Result<TreeSet<SysMenu>> menu() {
        Long currentUserId = ShiroUtils.getCurrentUserId();
        TreeSet<SysMenu> data = userService.getMenuTree(currentUserId);
        return Result.success(data);
    }
}
