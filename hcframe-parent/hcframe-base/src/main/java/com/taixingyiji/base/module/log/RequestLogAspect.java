package com.taixingyiji.base.module.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.log.annotation.SkipRequestLog;
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
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lhc
 */
@Component
@Aspect
public class RequestLogAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestLogAspect.class);
    private final SerializeConfig logSerializeConfig = new RequestLogSerializeConfig();

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
        if (shouldSkipLog(proceedingJoinPoint)) {
            return proceedingJoinPoint.proceed();
        }

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long timeCost = System.currentTimeMillis() - start;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null && Boolean.TRUE.equals(frameConfig.getShowControllerLog())) {
            try {
                logRequest(proceedingJoinPoint, attributes.getRequest(), result, timeCost);
            } catch (Exception logException) {
                LOGGER.warn("Failed to write controller request log", logException);
            }
        }

        return result;
    }

    @AfterThrowing(pointcut = "requestServer()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        if (shouldSkipLog(joinPoint)) {
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            try {
                logErrorRequest(joinPoint, e, attributes.getRequest());
            } catch (Exception logException) {
                LOGGER.warn("Failed to write controller error request log", logException);
                LOGGER.error("Controller request failed", e);
            }
        }
    }

    private boolean shouldSkipLog(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target != null ? AopUtils.getTargetClass(target) : method.getDeclaringClass();
        Method targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        return AnnotatedElementUtils.findMergedAnnotation(targetClass, SkipRequestLog.class) != null
                || AnnotatedElementUtils.findMergedAnnotation(targetMethod, SkipRequestLog.class) != null
                || AnnotatedElementUtils.findMergedAnnotation(method, SkipRequestLog.class) != null;
    }

    private void logErrorRequest(JoinPoint joinPoint, RuntimeException e, HttpServletRequest request) {
        RequestErrorInfo requestErrorInfo = new RequestErrorInfo();
        requestErrorInfo.setIp(request.getRemoteAddr());
        requestErrorInfo.setUrl(request.getRequestURL().toString());
        requestErrorInfo.setHttpMethod(request.getMethod());
        requestErrorInfo.setClassMethod(String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()));
        if (Boolean.TRUE.equals(frameConfig.getControllerLogParams())) {
            requestErrorInfo.setRequestParams(getRequestParamsByJoinPoint(joinPoint));
        }
        requestErrorInfo.setException(e);
        LOGGER.error("Error Request Info      : {}", trimValue(JSON.toJSONString(requestErrorInfo, logSerializeConfig)));
    }

    private void logRequest(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request, Object result, long timeCost) {
        String classMethod = String.format("%s.%s", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                proceedingJoinPoint.getSignature().getName());
        if (Boolean.TRUE.equals(frameConfig.getControllerLogDetail()) || shouldWriteDetailLog(timeCost)) {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setIp(request.getRemoteAddr());
            requestInfo.setUrl(request.getRequestURL().toString());
            requestInfo.setHttpMethod(request.getMethod());
            requestInfo.setClassMethod(classMethod);
            if (Boolean.TRUE.equals(frameConfig.getControllerLogParams())) {
                requestInfo.setRequestParams(getRequestParamsByProceedingJoinPoint(proceedingJoinPoint));
            }
            if (Boolean.TRUE.equals(frameConfig.getControllerLogResult())) {
                requestInfo.setResult(result);
            } else if (result != null) {
                requestInfo.setResult(result.getClass().getName());
            }
            requestInfo.setTimeCost(timeCost);
            LOGGER.info("Request Info      : {}", trimValue(JSON.toJSONString(requestInfo, logSerializeConfig)));
            return;
        }
        LOGGER.info("Request Summary   : ip={}, method={}, url={}, classMethod={}, timeCost={}ms, resultType={}",
                request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), classMethod, timeCost,
                result == null ? null : result.getClass().getName());
    }

    private boolean shouldWriteDetailLog(long timeCost) {
        Long slowMillis = frameConfig.getControllerLogSlowMillis();
        if (slowMillis != null && slowMillis >= 0 && timeCost >= slowMillis) {
            return true;
        }
        Double sampleRate = frameConfig.getControllerLogSampleRate();
        return sampleRate != null && sampleRate > 0D && ThreadLocalRandom.current().nextDouble() < sampleRate;
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
        for (int i = 0; i < paramValues.length; i++) {
            Object value = paramValues[i];
            if ((value instanceof ServletRequest) || (value instanceof ServletResponse)) {
                continue;
            }
            String paramName = paramNames != null && i < paramNames.length ? paramNames[i] : "arg" + i;
            requestParams.put(paramName, trimValue(value));
        }
        return requestParams;
    }

    private Object trimValue(Object value) {
        Integer maxLength = frameConfig.getControllerLogMaxValueLength();
        // Calling a DTO/collection's toString can itself traverse files and streams.
        if (!(value instanceof String text) || maxLength == null || maxLength < 0) {
            return value;
        }
        if (text.length() <= maxLength) {
            return value;
        }
        return text.substring(0, maxLength) + "...[truncated]";
    }

}
