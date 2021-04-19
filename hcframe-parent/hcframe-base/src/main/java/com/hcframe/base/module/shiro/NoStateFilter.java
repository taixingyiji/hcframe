package com.hcframe.base.module.shiro;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

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
public class NoStateFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

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
        try {
            getSubject(request, response).login(new AuthToken(token));
        } catch (Exception e) {
            e.printStackTrace();
            onLoginFail(response); //6、登录失败
            return false;
        }
        return true;
    }

    //登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error");
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
