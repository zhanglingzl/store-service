package com.rxr.store.core;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class CustomShiroRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = token.getPrincipal().toString();

        UserInfo user = new UserInfo();
        user.setName("test");
        user.setPassword("123456");

        // 加盐
        ByteSource salt = ByteSource.Util.bytes("test");
        // 将明文密码加密处理，如果数据库中存放的是加密后的数据，则不调用该方法
        SimpleHash sh = new SimpleHash("MD5",user.getPassword(),salt,1024);

        if(!("test").equals(userName)){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getName(),
                sh,
                salt,
                getName()
        );
        return authenticationInfo;
    }
}
