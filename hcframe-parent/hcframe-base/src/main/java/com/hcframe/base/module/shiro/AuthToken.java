package com.hcframe.base.module.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author lhc
 * @version 1.0
 * @className AuthToken
 * @date 2021年04月19日 2:56 下午
 * @description 实现shiro AuthenticationToken
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
