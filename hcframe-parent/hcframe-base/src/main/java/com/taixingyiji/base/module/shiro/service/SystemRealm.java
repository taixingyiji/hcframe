package com.taixingyiji.base.module.shiro.service;


import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.LinkedHashMap;

/**
 * @author lhc
 * @date 2020-07-28
 * @descirption 系统realm权限设置接口
 */
public interface SystemRealm {

    /**
     * 获取权限注入接口
     * @param user 用户信息
     * @return shiro权限注入对象
     */
    SimpleAuthorizationInfo setAuthoriztion(Object user);

    /**
     * 获取用户信息
     * @param userId 用户标识
     * @return 用户信息
     */
    Object findByUserId(String userId);

    /**
     * 设置Shiro访问的url
     * @return urlMap
     */
    LinkedHashMap<String, String> setShiroUrl();

}
