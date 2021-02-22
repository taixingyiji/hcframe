package com.hcframe.base.module.shiro;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * auth过滤器
 *
 * @Author lhc
 */
public class AuthFilter extends AuthenticatingFilter {

    /**
     * 生成自定义token
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);
        if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String myOrigin = httpServletRequest.getHeader("origin");
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, X-Access-Token, datasource-Key");
            httpResponse.setHeader("Access-Control-Allow-Origin", myOrigin);
            httpResponse.setCharacterEncoding("UTF-8");
            Map<String, Object> result = new HashMap<>();
            result.put("code", 3);
            result.put("msg", "未登陆");
            String json = JSON.toJSONString(result);
            httpResponse.getWriter().print(json);
            return null;
        }
        return new AuthToken(token);
    }

    /**
     * 步骤1.所有请求全部拒绝访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }

    /**
     * 步骤2，拒绝访问的请求，会调用onAccessDenied方法，onAccessDenied方法先获取 token，再调用executeLogin方法
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回
        String token = getRequestToken((HttpServletRequest) request);
        if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String myOrigin = httpServletRequest.getHeader("origin");
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, X-Access-Token, datasource-Key");
            httpResponse.setHeader("Access-Control-Allow-Origin", myOrigin);
            httpResponse.setCharacterEncoding("UTF-8");
            Map<String, Object> result = new HashMap<>();
            result.put("code", 3);
            result.put("msg", "未登陆");
            String json = JSON.toJSONString(result);
            httpResponse.getWriter().print(json);
            return false;
        }
        return executeLogin(request, response);
    }

    /**
     * 登陆失败时候调用
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //处理登录失败的异常
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String myOrigin = httpServletRequest.getHeader("origin");
        httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, X-Access-Token, datasource-Key");
        httpResponse.setHeader("Access-Control-Allow-Origin", myOrigin);
        httpResponse.setCharacterEncoding("UTF-8");
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 3);
            result.put("msg", "未登陆");
            String json = JSON.toJSONString(result);
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {
        }
        return false;
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader("X-Access-Token");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            if (StringUtils.isBlank(token)) {
                token = httpRequest.getParameter("token");
            }
        }
        return token;
    }
}
