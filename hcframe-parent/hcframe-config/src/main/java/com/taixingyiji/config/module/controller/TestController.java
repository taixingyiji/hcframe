package com.taixingyiji.config.module.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.data.module.BaseMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    BaseMapper baseMapper;

    @GetMapping("/test")
    public ResultVO<Object> getUser(HttpServletRequest request) {
        return ResultVO.getSuccess(SecurityUtils.getSubject().getPrincipal());
    }

    @GetMapping("/casProxy")
    public ResultVO<Object> getCasProxy(HttpServletRequest request) throws IOException {
        String serviceUrl = "http://192.168.1.130:8084/user/manage/ja";
        try {
//1、获取到AttributePrincipal对象
            AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
            if (principal == null) {
                return ResultVO.getFailed("用户未登录");
            }
            return ResultVO.getSuccess(principal);
        } catch (ClassCastException e) {
            return ResultVO.getSuccess(request.getUserPrincipal());
        }

        //2、获取对应的(PT)proxy ticket
//        String proxyTicket = principal.getProxyTicketFor(serviceUrl);
//        if (proxyTicket == null) {
//            return ResultVO.getFailed("PGT 或 PT 不存在");
//        }
//        URL url = new URL(serviceUrl + "?ticket=" + proxyTicket);// 不需要cookie，只需传入代理票据
//
//        HttpURLConnection conn;
//        conn = (HttpURLConnection) url.openConnection();
//        //使用POST方式
//        conn.setRequestMethod("GET");
//        // 设置是否向connection输出，因为这个是post请求，参数要放在
//        // Post 请求不能使用缓存
//        conn.setUseCaches(false);
//
//        //设置本次连接是否自动重定向
//        conn.setInstanceFollowRedirects(true);
//        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
//        // 意思是正文是urlencoded编码过的form参数
//        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
//        // 要注意的是connection.getOutputStream会隐含的进行connect。
//        conn.connect();
//
////        DataOutputStream out = new DataOutputStream(conn
////                .getOutputStream());
//////         正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
////        String content = "start=" + URLEncoder.encode("1901-01-01", "UTF-8")+"&end="+URLEncoder.encode("2018-01-01", "UTF-8");
////        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
////        out.writeBytes(content);
//////        流用完记得关
////        out.flush();
////        out.close();
//        StringBuilder result = new StringBuilder();
//        //获取响应
//        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null){
//            result.append(line);
//        }
//        reader.close();
//        //连接断了
//        conn.disconnect();

    }

    @GetMapping("/ivid")
    public ResultVO<String> ivid(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.invalidate();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultVO.getSuccess();
    }

    @GetMapping("/logout")
    public ResultVO<String> logout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultVO.getSuccess();
    }

}
