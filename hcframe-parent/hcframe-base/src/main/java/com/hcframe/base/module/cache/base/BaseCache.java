package com.hcframe.base.module.cache.base;

import org.springframework.cache.Cache;

/**
 * @author lhc
 * @description 缓存操作基础方法
 * @date 2020-12-31
 */
public interface BaseCache {

    <T> T get(String name, Object key, Class<T> tClass);

    void add(String name, Object key, Object data,Class<?> tClass);

    void update(String name, Object key,  Object data,Class<?> tClass);

    boolean delete(String name, Object key);

    Cache getCache(String name);
}
