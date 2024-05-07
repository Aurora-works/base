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

    /**
     * 跳转至登录页面
     */
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
    public Result<Object> loginByMobile(@RequestParam String mobilePhoneNumber, @RequestParam String code) {
        checkMobilePhoneNumber(mobilePhoneNumber);
        ShiroUtils.loginByMobile(mobilePhoneNumber, code);
        return Result.success();
    }

    /**
     * 发送验证码
     */
    @PostMapping(value = "/sendCode")
    @ResponseBody
    public Result<Object> sendCode(@RequestParam String mobilePhoneNumber) {
        checkMobilePhoneNumber(mobilePhoneNumber);
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

    /**
     * 手机号校验
     */
    private void checkMobilePhoneNumber(String mobilePhoneNumber) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        if (!pattern.matcher(mobilePhoneNumber).matches()) {
            throw new IllegalArgumentException("check mobile phone number");
        }
    }
}
