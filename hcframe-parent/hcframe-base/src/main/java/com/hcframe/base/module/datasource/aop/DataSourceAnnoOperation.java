package com.hcframe.base.module.datasource.aop;

import com.hcframe.base.module.datasource.annotation.DatasourceAnno;
import com.hcframe.base.module.datasource.dynamic.DBContextHolder;
import com.hcframe.base.module.shiro.dao.FtTokenDao;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lhc
 * @date 2020.09.23
 * @description 通过Aop形式，设置数据源，默认数据源设置为master节点
 */
@Order(5)
@Aspect
@Component
public class DataSourceAnnoOperation {

    @Autowired
    FtTokenDao osTokenMapper;

    @Around("@annotation(com.hcframe.base.module.datasource.annotation.DatasourceAnno)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 获取方法
        Method method = methodSignature.getMethod();
        // 获取方法上面的注解
        DatasourceAnno dataSourceAnno = method.getAnnotation(DatasourceAnno.class);
        String key = dataSourceAnno.value();
        DBContextHolder.clearDataSource();
        DBContextHolder.setDataSource(key);
        return pjp.proceed();
    }

}
