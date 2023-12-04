package com.taixingyiji.user.module.manage.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.redis.RedisUtil;
import com.taixingyiji.user.module.auth.service.AuthService;
import net.unicon.cas.client.configuration.CasClientConfigurationProperties;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

@Controller
@RequestMapping("cas")
public class CasController {

    final
    RedisUtil redisUtil;

    final
    CasClientConfigurationProperties casClientConfigurationProperties;

    final AuthService authService;

    public CasController(RedisUtil redisUtil,
                         CasClientConfigurationProperties casClientConfigurationProperties,
                         AuthService authService) {
        this.redisUtil = redisUtil;
        this.casClientConfigurationProperties = casClientConfigurationProperties;
        this.authService = authService;
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
            Map<Object, Object> hashMap = (Map<Object, Object>) redisUtil.get("session:"+token);
            AssertionImpl assertion = (AssertionImpl) hashMap.get("_const_cas_assertion_");
            AttributePrincipal attributePrincipal = assertion.getPrincipal();
            Map<String, Object> user = attributePrincipal.getAttributes();
            Long count = authService.getUserOs(String.valueOf(user.get("ID")));
            if (count == 0) {
                response.sendRedirect("http://" + webUrl + "/#/?token=noAuth");
            } else {
                webUrl = URLDecoder.decode(webUrl, "utf-8");
                response.sendRedirect("http://" + webUrl + "/#/?token=" + token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("userinfo")
    @ResponseBody
    public ResultVO<Object> getUserInfo() {
        return ResultVO.getSuccess(SecurityUtils.getSubject().getPrincipal());
    }

    @GetMapping("/logout")
    @ResponseBody
    public ResultVO<String> logout(HttpServletRequest request, @RequestHeader("X-Access-Token") String token) {
        Cookie cookie = new Cookie("X-Access-Token", null);
        cookie.setMaxAge(0);
        String headerToken = request.getHeader("X-Access-Token");
        redisUtil.del("session:"+ token);
        redisUtil.del("session:"+ headerToken);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultVO.getSuccess(casClientConfigurationProperties.getServerUrlPrefix()+"/logout");
    }
}
