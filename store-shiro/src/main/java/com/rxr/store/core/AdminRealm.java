package com.rxr.store.core;

import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.entities.User;
import com.rxr.store.core.util.JWTHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 需要重写此方法
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        user.getRoles().forEach(role -> {
            authorizationInfo.addRole(role.getRole());
            role.getPermissions().forEach(permission -> authorizationInfo.addStringPermission(permission.getUrl()));
        });
        return authorizationInfo;
    }

    /**
     *
     * @param authToken 用户认证
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        String token = (String) authToken.getPrincipal();
        String loginName = JWTHelper.getLoginName(token);
        if(StringUtils.isBlank(loginName)) {
            throw new AuthenticationException();
        }
        User user = userService.findUserByLoginName(loginName);
        if(user == null) {
            throw new AuthenticationException();
        }

        if(!JWTHelper.verify(token,loginName, user.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        user.setToken(token);
        return new SimpleAuthenticationInfo(
                user,
                token,
                getName()
        );
    }
}
