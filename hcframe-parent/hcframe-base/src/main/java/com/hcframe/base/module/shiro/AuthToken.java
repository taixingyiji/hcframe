package com.hcframe.base.module.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义AuthenticationToken类
 * @Author lhc
 */
public class AuthToken implements AuthenticationToken {

    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
