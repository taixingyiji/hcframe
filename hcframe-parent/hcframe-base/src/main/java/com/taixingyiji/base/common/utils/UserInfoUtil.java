package com.taixingyiji.base.common.utils;

import com.taixingyiji.base.module.shiro.FtToken;
import com.taixingyiji.base.module.shiro.service.ShiroService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class UserInfoUtil<T> {

    @Resource
    ShiroService shiroService;

    /**
     * 通过request返回用户信息
     * @param request request
     * @return 用户类型
     */
    public T getUserInfoByRequest(HttpServletRequest request) {
        String token = request.getHeader("X-Access-Token");
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        FtToken ftToken = shiroService.findByToken(token);
        Date expireData= ftToken.getExpireTime();
        Date date = new Date();
        if (date.getTime() > expireData.getTime()) {
            return null;
        }
        return (T) shiroService.findByUserId(ftToken.getUserId());
    }

    /**
     * 获取当前用户信息
     * @param <T> 用户实体类泛型
     * @return 返回结果
     */
    public static <T> T getUserInfo(){
        return (T)SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取当前用户token
     * @param request
     * @return
     */
    public static String getUserToken(HttpServletRequest request){
        String token = request.getHeader("X-Access-Token");
        //如果header中不存在token，则从参数中获取token
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
                token = request.getParameter("token");
        }
        return token;
    }
}
