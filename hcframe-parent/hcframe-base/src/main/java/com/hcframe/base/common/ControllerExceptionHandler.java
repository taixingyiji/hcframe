package com.hcframe.base.common;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * @description 异常信息拦截类，如果程序运行过程中抛出异常，会将异常拦截下来，并返回前端一个包含异常信息的Json数据
 */

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVO handleServiceException(
            Exception e) {
        if (e instanceof AuthorizationException) {
            return ResultVO.getNoAuthorization();
        } else if (e instanceof AuthenticationException) {
            return ResultVO.getFailed("用户名或密码错误");
        } else if (e instanceof RuntimeException) {
            System.out.println("handleServiceException");
            e.printStackTrace();
            //封装错误信息
            return ResultVO.getException(e);
        } else {
            return ResultVO.getException(e);
        }
    }
    //.......
}
