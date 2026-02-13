package com.taixingyiji.base.module.datasource.aop;

import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.datasource.dao.DatasourceConfigDao;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.utils.DataUnit;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author lhc
 * @date 2020-09-23
 * @description 通过注解形式设置数据源，优先级为最高。接口添加注解后，以注解数据源为准。
 */
@Aspect
@Order(1)
@Component
public class DataSourceAop {

    public static boolean isMulti;

    // 通过yml文件获取host
    @Autowired
    public void setHost(FrameConfig config) {
        DataSourceAop.isMulti = config.getMultiDataSource();
    }

    @Autowired
    DatasourceConfigDao datasourceConfigDao;

    private final static Logger logger = LoggerFactory.getLogger(DataSourceAop.class);

    //这个切点的表达式需要根据自己的项目来写
    @Pointcut("execution(public * com..*.controller..*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes != null) {
//            HttpServletRequest request = attributes.getRequest();
//            if (isMulti) {
//                String key = request.getParameter("datasourceKey");
//                if (StringUtils.isBlank(key)) {
//                    DBContextHolder.setDataSource(DataUnit.MASTER);
//                } else {
//                    DBContextHolder.setDataSource(key);
//                }
//            }
//        }
    }

    @After("log()")
    public void doAfter() {
//        if (isMulti) {
//            DBContextHolder.clearDataSource();
//        }
    }
}




