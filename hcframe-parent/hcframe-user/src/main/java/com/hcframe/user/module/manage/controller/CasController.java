package com.hcframe.user.module.manage.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.redis.RedisUtil;
import net.unicon.cas.client.configuration.CasClientConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@Controller
@RequestMapping("cas")
public class CasController {

    final
    RedisUtil redisUtil;

    final
    CasClientConfigurationProperties casClientConfigurationProperties;

    public CasController(RedisUtil redisUtil, CasClientConfigurationProperties casClientConfigurationProperties) {
        this.redisUtil = redisUtil;
        this.casClientConfigurationProperties = casClientConfigurationProperties;
    }


    @GetMapping("valid")
    public ResultVO<String> casValid(HttpServletResponse response, HttpServletRequest request,String webUrl) {
        String token = "";
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("X-Access-Token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    response.addCookie(cookie);
                    break;
                }
            }
            webUrl = URLDecoder.decode(webUrl, "utf-8");
            response.sendRedirect("http://"+webUrl+"/#/?token=" + token );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("userinfo")
    @ResponseBody
    public ResultVO<Object> getUserInfo(String token) {
        return ResultVO.getSuccess(redisUtil.hget("session", token));
    }

    @GetMapping("/logout")
    @ResponseBody
    public ResultVO<String> logout(HttpServletRequest request, @CookieValue("X-Access-Token") String token) {
        Cookie cookie = new Cookie("X-Access-Token", null);
        cookie.setMaxAge(0);
        String headerToken = request.getHeader("X-Access-Token");
        redisUtil.hdel("session", token);
        redisUtil.hdel("session", headerToken);
        return ResultVO.getSuccess(casClientConfigurationProperties.getServerUrlPrefix()+"/cas/logout");
    }
}
