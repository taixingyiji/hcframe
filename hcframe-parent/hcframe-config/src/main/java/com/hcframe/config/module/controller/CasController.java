package com.hcframe.config.module.controller;

import com.hcframe.base.common.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CasController {

    @GetMapping("valid")
    public ResultVO<String> casValid(HttpServletResponse response, HttpServletRequest request){
        String token = "";
        try {
            Cookie[] cookies= request.getCookies();
            for (Cookie cookie : cookies) {
                if ("X-Access-Token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
            response.sendRedirect("http://192.168.1.130:9527/?token="+token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
