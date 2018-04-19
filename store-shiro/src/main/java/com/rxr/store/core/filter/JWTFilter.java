package com.rxr.store.core.filter;

import com.rxr.store.core.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String authzHeader = getAuthzHeader(request);
        //去除基于basick判断
        //return authzHeader != null && isLoginAttempt(authzHeader);
        return authzHeader != null;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String authorizationHeader = getAuthzHeader(request);
        return authorizationHeader == null ? null : new JWTToken(authorizationHeader);
    }

}
