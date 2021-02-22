package com.hcframe.base.module.datasource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lhc
 * @description 规定数据源类型注解
 * @date 2020-09-23
 */
// 方法注解
@Target(ElementType.METHOD)
// 运行时可见
@Retention(RetentionPolicy.RUNTIME)
public @interface DatasourceAnno {
    // 填写数据源类型key
    String value();
}
