package com.taixingyiji.base.module.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略字段
 *
 * @author lhc
 */
// 方法注解
@Target(ElementType.FIELD)
// 运行时可见
@Retention(RetentionPolicy.RUNTIME)
public @interface DataIgnore {
}
