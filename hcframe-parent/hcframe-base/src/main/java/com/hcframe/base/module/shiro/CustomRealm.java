package com.hcframe.base.module.shiro;


import com.hcframe.base.common.config.FrameConfig;
import com.hcframe.base.module.shiro.service.ShiroService;
import com.hcframe.base.module.shiro.service.SystemRealm;
import com.hcframe.redis.RedisUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


/**
 * @author lhc
 * @version 1.0
 * @className CustomRealm
 * @date 2021年04月19日 2:56 下午
 * @description 自定义Realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private ShiroService shiroService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    FrameConfig frameConfig;

    @Resource
    private SystemRealm systemRealm;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object user = principalCollection.getPrimaryPrincipal();
        return systemRealm.setAuthoriztion(user);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        String accessToken = (String) token.getPrincipal();
        String userId;
        if (frameConfig.getCas()) {
            Map<Object, Object> hashMap = (Map<Object, Object>) redisUtil.hget("session", accessToken);
            Long expireTime = (Long) hashMap.get("expireTime");
            expireTime = expireTime * 1000;
            AssertionImpl assertion = (AssertionImpl) hashMap.get("_const_cas_assertion_");
            Date validDate = assertion.getAuthenticationDate();
            AttributePrincipal attributePrincipal = assertion.getPrincipal();
            if (validDate.getTime() + expireTime < System.currentTimeMillis()) {
                throw new IncorrectCredentialsException("token失效，请重新登录");
            }
            return new SimpleAuthenticationInfo(attributePrincipal.getAttributes(), accessToken, this.getName());
        } else if (frameConfig.getIsRedisLogin()) {
            Map<Object, Object> hashMap = (Map<Object, Object>) redisUtil.hget("tokenSession", accessToken);
            userId = (String) hashMap.get("userId");
            if (userId == null) {
                throw new IncorrectCredentialsException("token失效，请重新登录");
            }
            if (frameConfig.getSingleClientLogin()) {
                String tokenStr = (String) redisUtil.hget("session", String.valueOf(userId));
                if (tokenStr == null || !tokenStr.equals(accessToken)) {
                    redisUtil.del(accessToken);
                    throw new IncorrectCredentialsException("token失效，请重新登录");
                }
            }
            Date expireTime = (Date) hashMap.get("expireTime");
            System.out.println(expireTime);
            if (expireTime.getTime() < System.currentTimeMillis()) {
                redisUtil.del(accessToken);
                throw new IncorrectCredentialsException("token失效，请重新登录");
            }
        } else {
            //1. 根据accessToken，查询用户信息
            FtToken tokenEntity = shiroService.findByToken(accessToken);
            userId = tokenEntity.getUserId();
            //2. token失效
            if (tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
                throw new IncorrectCredentialsException("token失效，请重新登录");
            }
        }
        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        Object user = shiroService.findByUserId(userId);
        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if (user == null) {
            throw new UnknownAccountException("用户不存在!");
        }
        //5. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        return new SimpleAuthenticationInfo(user, accessToken, this.getName());
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof AuthToken;
    }
}

