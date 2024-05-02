package org.aurora.base.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Pattern;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    /**
     * 密码登录
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public Result<Object> login(@RequestParam String username, @RequestParam String password) {
        ShiroUtils.loginByPassword(username, password);
        return Result.success();
    }

    /**
     * 短信登录
     */
    @PostMapping(value = "/loginByMobile")
    @ResponseBody
    public Result<Object> loginByMobile(@RequestParam String mobile, @RequestParam String code) {
        checkMobileNumber(mobile);
        ShiroUtils.loginByMobile(mobile, code);
        return Result.success();
    }

    /**
     * 发送验证码
     */
    @PostMapping(value = "/sendCode")
    @ResponseBody
    public Result<Object> sendCode(@RequestParam String mobile) {
        checkMobileNumber(mobile);
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

    private void checkMobileNumber(String mobile) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        if (!pattern.matcher(mobile).matches()) {
            throw new IllegalArgumentException("check mobile number");
        }
    }
}
