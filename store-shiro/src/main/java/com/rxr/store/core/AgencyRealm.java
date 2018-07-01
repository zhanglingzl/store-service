package com.rxr.store.core;

import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.common.entities.Agency;
import com.rxr.store.core.util.JWTHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class AgencyRealm extends AuthorizingRealm {
    @Autowired
    private AgencyService agencyService;

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
       return null;
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
        Agency agency = agencyService.findAgencyByWechatId(loginName);
        //Agency agency = new Agency();
        if(agency == null) {
            throw new AuthenticationException();
        }

        if(!JWTHelper.verify(token,loginName)) {
            throw new AuthenticationException("Username or password error");
        }
        agency.setToken(token);
        return new SimpleAuthenticationInfo(
                agency,
                token,
                getName()
        );
    }
}
