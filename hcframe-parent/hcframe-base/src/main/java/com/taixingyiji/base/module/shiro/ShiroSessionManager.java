package com.taixingyiji.base.module.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author lhc
 * @version 1.0
 * @className ShiroSessionManager
 * @date 2021年04月19日 2:56 下午
 * @description 自定义 Session Manager - 从 Header 中提取 Token
 */
public class ShiroSessionManager extends DefaultWebSessionManager {

    private static final String AUTHORIZATION = "X-Access-Token";
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public ShiroSessionManager(){
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response){
        String id = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            id = httpRequest.getHeader(AUTHORIZATION);
            System.out.println("Token from header: " + id);
        }
        
        if(StringUtils.isEmpty(id)){
            // 如果没有携带 token 则按照父类的方式在 cookie 进行获取
            return super.getSessionId(request, response);
        } else {
            // 如果请求头中有 token 则其值为 sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
    }
}
