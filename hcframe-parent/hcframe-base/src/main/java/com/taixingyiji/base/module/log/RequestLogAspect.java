package com.taixingyiji.base.module.log;

import com.alibaba.fastjson.JSON;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.log.model.RequestErrorInfo;
import com.taixingyiji.base.module.log.model.RequestInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author lhc
 */
@Component
@Aspect
public class RequestLogAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestLogAspect.class);

    public RequestLogAspect(FrameConfig frameConfig) {
        this.frameConfig = frameConfig;
    }

    @Pointcut("execution(public * com..*.controller..*(..))")
    public void requestServer() {
    }

    final
    FrameConfig frameConfig;

    @Around("requestServer()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            if (frameConfig.getShowControllerLog()) {
                RequestInfo requestInfo = new RequestInfo();
                requestInfo.setIp(request.getRemoteAddr());
                requestInfo.setUrl(request.getRequestURL().toString());
                requestInfo.setHttpMethod(request.getMethod());
                requestInfo.setClassMethod(String.format("%s.%s", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                        proceedingJoinPoint.getSignature().getName()));
                requestInfo.setRequestParams(getRequestParamsByProceedingJoinPoint(proceedingJoinPoint));
                requestInfo.setResult(result);
                requestInfo.setTimeCost(System.currentTimeMillis() - start);
                LOGGER.info("Request Info      : {}", JSON.toJSONString(requestInfo));
            }
        }

        return result;
    }

    @AfterThrowing(pointcut = "requestServer()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            RequestErrorInfo requestErrorInfo = new RequestErrorInfo();
            requestErrorInfo.setIp(request.getRemoteAddr());
            requestErrorInfo.setUrl(request.getRequestURL().toString());
            requestErrorInfo.setHttpMethod(request.getMethod());
            requestErrorInfo.setClassMethod(String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName()));
            requestErrorInfo.setRequestParams(getRequestParamsByJoinPoint(joinPoint));
            requestErrorInfo.setException(e);
            LOGGER.error("Error Request Info      : {}", JSON.toJSONString(requestErrorInfo));
        }
    }

    /**
     * 获取入参
     *
     * @param proceedingJoinPoint
     * @return
     */
    private Map<String, Object> getRequestParamsByProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = proceedingJoinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = joinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        Map<String, Object> requestParams = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            if ((value instanceof HttpServletRequest) || (value instanceof HttpServletResponse)) {
                continue;
            }
            //如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                // 获取文件名
                value = file.getOriginalFilename();
            }
            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }

}
