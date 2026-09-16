package com.taixingyiji.base.module.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 跳过 RequestLogAspect 的请求日志处理，包括正常日志和异常日志。
 * 标记方法时仅对该方法生效，标记类时对该类的所有接口生效。
 * 不读取请求参数、不解析返回值，业务返回值和异常仍按原样传递。
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipRequestLog {
}
