package com.hcframe.base.module.cache.base.impl;

import com.hcframe.base.common.ServiceException;
import com.hcframe.base.module.cache.base.BaseCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


/**
 * @author lhc
 * @description 缓存操作基础方法实现类
 * @date 2020-12-31
 */
@Component
public class BaseCacheImpl implements BaseCache {

    private final static Logger logger = LoggerFactory.getLogger(BaseCacheImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Override
    public <T> T get(String name, Object key, Class<T> tClass) {
        Cache cache = getCache(name);
        return cache.get(key, tClass);
    }

    @Override
    public void add(String name, Object key,  Object data,Class<?> tClass) {
        Cache cache = getCache(name);
        if (tClass.isInstance(data)) {
            cache.put(key, tClass.cast(data));
        } else {
            throw new ServiceException("data 类型不匹配！");
        }
    }

    @Override
    public void update(String name, Object key, Object data,Class<?> tClass) {
        Cache cache = getCache(name);
        if (tClass.isInstance(data)) {
            cache.put(key, tClass.cast(data));
        } else {
            throw new ServiceException("data 类型不匹配！");
        }
    }

    @Override
    public boolean delete(String name, Object key) {
        Cache cache = getCache(name);
        return cache.evictIfPresent(key);
    }

    @Override
    public Cache getCache(String name) {
        return cacheManager.getCache(name);
    }
}
