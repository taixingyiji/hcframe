package com.taixingyiji.base.module.cache;

import com.taixingyiji.base.module.cache.emum.CacheType;

import java.util.List;

public interface CacheService {

    void initTableCache();

    <T> T getCacheValue(CacheType name, Object key, Class<T> tClass);

    boolean save(CacheType name,Object key,Object data,Class<?> tClass) ;

    boolean save(String key, Object data, Class<?> tClass);

    boolean delete(String typeName);

    boolean saveBatich(List<Object> key);

    boolean deleteBatch(String ids);
}
