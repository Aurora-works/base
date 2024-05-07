package org.aurora.base.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.aurora.base.util.enums.Status;
import org.aurora.base.util.enums.UserType;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Map;
import java.util.Set;

public class SysUserRealm extends AuthorizingRealm {

    @Lazy
    @Autowired
    private SysUserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        Long userId = user.getId();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Map<String, Set<String>> map = userService.getAuthorizationInfo(userId);
        authorizationInfo.setRoles(map.get("roles"));
        authorizationInfo.setStringPermissions(map.get("permissions"));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        SysUserToken token = (SysUserToken) authenticationToken;
        String principal = token.getUsername();
        SysUser user = switch (token.getLoginType()) {
            case PASSWORD -> userService.findByUsername(principal);
            case MOBILE -> userService.findByMobile(principal);
        };
        if (user == null || !UserType.ADMIN.getKey().equals(user.getUserType())) {
            throw new UnknownAccountException();
        }
        if (!Status.ENABLED.getKey().equals(user.getStatus())) {
            throw new LockedAccountException();
        }
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );
    }

    private static final String OR_OPERATOR = " or ";
    private static final String AND_OPERATOR = " and ";
    private static final String NOT_OPERATOR = "not ";

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if (permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for (String orPermission : permissions) {
                if (isPermittedWithNotOperator(principals, orPermission)) {
                    return true;
                }
            }
            return false;
        } else if (permission.contains(AND_OPERATOR)) {
            String[] permissions = permission.split(AND_OPERATOR);
            for (String andPermission : permissions) {
                if (!isPermittedWithNotOperator(principals, andPermission)) {
                    return false;
                }
            }
            return true;
        }
        return isPermittedWithNotOperator(principals, permission);
    }

    private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
        if (permission.startsWith(NOT_OPERATOR)) {
            return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
        }
        return super.isPermitted(principals, permission);
    }
}
