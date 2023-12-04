package com.taixingyiji.base.module.cache.impl;

import com.taixingyiji.base.module.cache.CacheService;
import com.taixingyiji.base.module.cache.emum.CacheType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatasourceCache implements CacheService {

    @Autowired
    private CacheManager cacheManager;

    private final static Logger logger = LoggerFactory.getLogger(DatasourceCache.class);

    @Override
    public void initTableCache() {
        logger.info("加载DatasorceCache");
    }

    @Override
    public <T> T getCacheValue(CacheType name, Object key, Class<T> tClass) {
        return null;
    }

    @Override
    public boolean save(CacheType name, Object key, Object data, Class<?> tClass) {
        return false;
    }

    @Override
    public boolean save(String key, Object data, Class<?> tClass) {
        return false;
    }

    @Override
    public boolean delete(String typeName) {
        return false;
    }

    @Override
    public boolean saveBatich(List<Object> key) {
        return false;
    }

    @Override
    public boolean deleteBatch(String ids) {
        return false;
    }
}
