package com.hcframe.config.module.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CasController {

    @Autowired
    RedisUtil redisUtil;

//    final
//    CasClientConfigurationProperties casClientConfigurationProperties;
//
//    public CasController(CasClientConfigurationProperties casClientConfigurationProperties) {
//        this.casClientConfigurationProperties = casClientConfigurationProperties;
//    }

    @GetMapping("valid")
    public ResultVO<String> casValid(HttpServletResponse response, HttpServletRequest request) {
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
            response.sendRedirect("http://192.168.1.130:9527/#/login?token=" + token + "&redirect=%2Fdashboard");
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

    @GetMapping("/cas/logout")
    @ResponseBody
    public ResultVO<String> logout(HttpServletResponse response, @CookieValue("X-Access-Token") String token) {
        Cookie cookie = new Cookie("X-Access-Token", null);
        cookie.setMaxAge(0);
        redisUtil.hdel("session", token);
        return ResultVO.getSuccess("http://192.168.1.131:8080/cas/logout");
    }

    @GetMapping("/cas")
    public String getCasHtml() {
        return "cas";
    }
}
