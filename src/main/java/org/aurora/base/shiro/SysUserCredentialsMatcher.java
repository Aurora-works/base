package org.aurora.base.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class SysUserCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        SysUserToken userToken = (SysUserToken) token;
        if (LoginType.MOBILE.equals(userToken.getLoginType())) {
            // TODO 短信登录校验
            return false;
        }
        return super.doCredentialsMatch(token, info);
    }
}
