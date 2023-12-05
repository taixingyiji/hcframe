package com.taixingyiji.config.common.config;

import com.taixingyiji.base.module.auth.dao.FtUserDao;
import com.taixingyiji.base.module.auth.entity.FtUser;
import com.taixingyiji.base.module.shiro.service.ShiroType;
import com.taixingyiji.base.module.shiro.service.SystemRealm;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author lhc
 * @date 2021-02-05
 * @decription shiro 配置类
 */
@Component
public class ShiroRealmConfig implements SystemRealm {

    final
    FtUserDao ftUserDao;

    public ShiroRealmConfig(FtUserDao ftUserDao) {
        this.ftUserDao = ftUserDao;
    }

    /**
     * 根据用户信息注入权限
     * @param user 用户信息
     * @return 权限信息
     */
    @Override
    public SimpleAuthorizationInfo setAuthoriztion(Object user) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 根据用户Id查询用户信息并注入到shiro框架中
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public Object findByUserId(String userId) {
        return ftUserDao.selectOne(FtUser.builder().userId(Integer.parseInt(userId)).build());
    }

    /**
     * 配置拦截及放行路径
     * @return 返回拦截及放行路径Map
     */
    @Override
    public LinkedHashMap<String, String> setShiroUrl() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // 用户登陆
        map.put("/ftUser/login", ShiroType.ANON);
        // Vue静态资源
        map.put("/img/**", ShiroType.ANON);
        map.put("/static/**", ShiroType.ANON);
        map.put("/tinymce/**", ShiroType.ANON);
        map.put("/favicon.ico", ShiroType.ANON);
        map.put("/manifest.json", ShiroType.ANON);
        map.put("/robots.txt", ShiroType.ANON);
        map.put("/precache*", ShiroType.ANON);
        map.put("/service-worker.js", ShiroType.ANON);
        // swagger UI 静态资源
        map.put("/swagger-ui.html",ShiroType.ANON);
        map.put("/doc.html",ShiroType.ANON);
        map.put("/swagger-resources/**",ShiroType.ANON);
        map.put("/webjars/**",ShiroType.ANON);
        map.put("/v2/api-docs",ShiroType.ANON);
        map.put("/v2/api-docs-ext",ShiroType.ANON);
        map.put("/swagger/**",ShiroType.ANON);
        // druid 资源路径
        map.put("/druid/**",ShiroType.ANON);
        map.put("/cas/valid",ShiroType.ANON);
        map.put("/test",ShiroType.AUTH);
        // 其余路径均拦截
        map.put("/**", ShiroType.ANON);
        return map;
    }

}
