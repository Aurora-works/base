package org.aurora.base.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.common.Result;
import org.aurora.base.common.dict.SysParam;
import org.aurora.base.common.validation.Mobile;
import org.aurora.base.common.validation.Password;
import org.aurora.base.service.sys.SysParamService;
import org.aurora.base.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    public LoginController(SysParamService paramService) {
        this.paramService = paramService;
    }

    private final SysParamService paramService;

    /**
     * 跳转至登录页面
     */
    @GetMapping(value = "/login")
    public String login(Model model) {
        String defaultThemes = paramService.getValueByCode(SysParam.SYS_DEFAULT_THEMES.getCode());
        model.addAttribute("defaultThemes", defaultThemes);
        return "login";
    }

    /**
     * 密码登录
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public Result<Object> login(
            @RequestParam String username,
            @RequestParam @Password String password) {
        ShiroUtils.loginByPassword(username, password);
        return Result.success();
    }

    /**
     * 短信登录
     */
    @PostMapping(value = "/loginByMobile")
    @ResponseBody
    public Result<Object> loginByMobile(
            @RequestParam @Mobile String mobilePhoneNumber,
            @RequestParam String code) {
        ShiroUtils.loginByMobile(mobilePhoneNumber, code);
        return Result.success();
    }

    /**
     * 发送验证码
     */
    @PostMapping(value = "/sendCode")
    @ResponseBody
    public Result<Object> sendCode(
            @RequestParam @Mobile String mobilePhoneNumber) {
        // TODO 发送验证码
        return Result.success();
    }

    /**
     * 退出系统
     */
    @PostMapping(value = "/logout")
    @ResponseBody
    @RequiresAuthentication
    public Result<Object> logout() {
        ShiroUtils.logout();
        return Result.success();
    }
}
