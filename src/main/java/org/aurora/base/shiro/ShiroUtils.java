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

    public static void encryptPassword(SysUser user) {
        user.setSalt(saltGenerator.nextBytes().toHex());
        user.setPassword(new SimpleHash(
                hashAlgorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                hashIterations
        ).toHex());
    }

    public static Long getCurrentUserId() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return TodoUser.USER_NO_LOGIN.getUserId();
        }
        return user.getId();
    }

    public static void loginByPassword(String username, String password) {
        SysUserToken token = new SysUserToken(username, password, LoginType.PASSWORD);
        SecurityUtils.getSubject().login(token);
    }

    public static void loginByMobile(String mobilePhoneNumber, String code) {
        SysUserToken token = new SysUserToken(mobilePhoneNumber, code, LoginType.MOBILE);
        SecurityUtils.getSubject().login(token);
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }
}
