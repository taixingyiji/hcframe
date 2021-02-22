package com.hcframe.base.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware{
    private static ApplicationContext applicationContext;
    //获取上下文
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //设置上下文
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    //通过名字获取上下文中的bean
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
    //通过类型获取上下文中的bean
    public static Object getBean(Class<?> requiredType){
        return applicationContext.getBean(requiredType);
    }
}
