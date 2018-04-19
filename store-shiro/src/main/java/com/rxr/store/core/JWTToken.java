package com.rxr.store.core;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;

@AllArgsConstructor
@ToString
public class JWTToken implements AuthenticationToken {
    private String token;
    @Override
    public Object getPrincipal() {
        return format(this.token);
    }

    @Override
    public Object getCredentials() {
        return format(this.token);
    }

    private String format(String token) {
        if(StringUtils.isNotBlank(this.token) && this.token.startsWith("Bearer")) {
            this.token = this.token.replace("Bearer","").trim();
        }
        return token;
    }
}
