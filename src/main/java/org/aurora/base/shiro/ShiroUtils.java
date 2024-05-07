package org.aurora.base.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.util.enums.TodoUser;

public class ShiroUtils {

    // spring-shiro.xml
    private static final String hashAlgorithmName = "SHA-256";
    private static final int hashIterations = 1;
    private static final RandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();

    /**
     * 密码加密
     */
    public static void encryptPassword(SysUser user) {
        user.setSalt(saltGenerator.nextBytes().toHex());
        user.setPassword(new SimpleHash(
                hashAlgorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                hashIterations
        ).toHex());
    }

    /**
     * 获取当前用户id
     */
    public static Long getCurrentUserId() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return TodoUser.USER_NO_LOGIN.getUserId();
        }
        return user.getId();
    }

    /**
     * 密码登录
     */
    public static void loginByPassword(String username, String password) {
        SysUserToken token = new SysUserToken(username, password, LoginType.PASSWORD);
        SecurityUtils.getSubject().login(token);
    }

    /**
     * 短信登录
     */
    public static void loginByMobile(String mobilePhoneNumber, String code) {
        SysUserToken token = new SysUserToken(mobilePhoneNumber, code, LoginType.MOBILE);
        SecurityUtils.getSubject().login(token);
    }

    /**
     * 退出系统
     */
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 检查权限
     */
    public static void checkPermission(String permission) {
        SecurityUtils.getSubject().checkPermission(permission);
    }
}
