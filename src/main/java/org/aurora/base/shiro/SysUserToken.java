package org.aurora.base.shiro;

import lombok.Getter;
import org.apache.shiro.authc.UsernamePasswordToken;

@Getter
public class SysUserToken extends UsernamePasswordToken {

    private final LoginType loginType;

    SysUserToken(String username, String password, LoginType loginType) {
        super(username, password);
        this.loginType = loginType;
    }
}
