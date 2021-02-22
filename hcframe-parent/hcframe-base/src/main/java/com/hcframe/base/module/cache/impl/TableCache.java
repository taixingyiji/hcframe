package com.hcframe.base.module.cache.impl;

import com.hcframe.base.common.utils.JudgeException;
import com.hcframe.base.module.cache.CacheService;
import com.hcframe.base.module.cache.base.BaseCache;
import com.hcframe.base.module.cache.emum.CacheType;
import com.hcframe.base.module.data.controller.TableController;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.tableconfig.dao.OsSysTableMapper;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lhc
 * @date 2020-12-30
 * @description 初始化配置表缓存操作类
 */
@Component
@Qualifier("table")
public class TableCache implements CacheService {

    private final static Logger logger = LoggerFactory.getLogger(TableController.class);
    // 基表开头
    public static final String BASE = "BASE_";
    public static final String TABLE = "table";
    // 状态
    public static ConcurrentMap<String, Integer> status = new ConcurrentHashMap<String, Integer>();

    @Autowired
    OsSysTableMapper osSysTableMapper;

    @Qualifier(BaseMapperImpl.BASE)
    @Autowired
    BaseMapper baseMapper;

    @Autowired
    BaseCache baseCache;

    // 初始化缓存加载
    @Override
    public void initTableCache() {
        List<OsSysTable> list = osSysTableMapper.selectAll();
        // 将数据写入缓存
        for (OsSysTable osSysTable : list) {
            logger.info("loading tableInfo : " + osSysTable.getTableContent());
            // 写入缓存
            baseCache.add(
                    CacheType.tableCache.toString(),
                    osSysTable.getTableAlias(),
                    osSysTableMapper.getTableAllInfo(osSysTable.getTableAlias()),
                    OsSysTable.class
            );
            if (osSysTable.getTableName().startsWith(BASE)) {
                logger.info("loading baseTable : " + osSysTable.getTableName());
                List<Map<String, Object>> baseList = baseMapper.selectAll(osSysTable.getTableName());
                // 写入缓存
                baseCache.add(
                        CacheType.baseCache.toString(),
                        osSysTable.getTableAlias(),
                        baseList,
                        List.class
                );
            }
        }
        logger.info("data init finished");
    }

    @Override
    public <T> T getCacheValue(CacheType name, Object key, Class<T> tClass) {
        T t = baseCache.get(name.toString(), key, tClass);
        // 若缓存过期，重新加载
        if (t == null) {
            switch (name) {
                case tableCache:
                    OsSysTable osSysTable = osSysTableMapper.getTableAllInfo((String) key);
                    JudgeException.isNull(osSysTable, "can not find key " + key + " in cache which cache name is " + name);
                    baseCache.add(name.toString(), key, osSysTableMapper.getTableAllInfo((String)key), OsSysTable.class);
                    break;
                case baseCache:
                    OsSysTable osSysTable1 = getCacheValue(CacheType.tableCache, key, OsSysTable.class);
                    List<Map<String, Object>> baseList = baseMapper.selectAll(osSysTable1.getTableName());
                    JudgeException.isNull(baseList, "can not find key " + key + " in cache which cache name is " + name);
                    baseCache.add(name.toString(), key, baseList, List.class);
                    break;
            }
        }
        return baseCache.get(name.toString(), key, tClass);
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
        return true;
    }

    public boolean isRuning() {
        return !status.isEmpty();
    }
}
