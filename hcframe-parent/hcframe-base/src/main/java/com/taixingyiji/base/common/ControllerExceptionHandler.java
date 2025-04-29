package com.taixingyiji.base.common;

import com.taixingyiji.base.module.cache.impl.DatasourceCache;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/***
 * @description 异常信息拦截类，如果程序运行过程中抛出异常，会将异常拦截下来，并返回前端一个包含异常信息的Json数据
 */

@ControllerAdvice
public class ControllerExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

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
            logger.error(e.getMessage(),e);
            //封装错误信息
            return ResultVO.getException(e);
        } else if(e instanceof MethodArgumentNotValidException){
            String defaultMessage = Objects.requireNonNull(((MethodArgumentNotValidException) e).getBindingResult().getFieldError()).getDefaultMessage();
            logger.error("请求参数验证异常：", e);
            return ResultVO.getFailed(defaultMessage);
        }else {
            return ResultVO.getException(e);
        }
    }
    //.......
}
