package com.hcframe.base.common.utils;

import com.hcframe.base.module.cache.emum.CacheType;
import com.hcframe.base.module.cache.impl.TableCache;
import com.hcframe.base.module.tableconfig.dao.OsSysTableMapper;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TableNameUtil {

    final
    OsSysTableMapper osSysTableMapper;

    private final TableCache tableCache;

    public TableNameUtil(OsSysTableMapper osSysTableMapper, TableCache tableCache) {
        this.osSysTableMapper = osSysTableMapper;
        this.tableCache = tableCache;
    }

    public OsSysTable getTableName(String typeName) {
        // 缓存中读取
        OsSysTable osSysTable = tableCache.getCacheValue(CacheType.tableCache, typeName, OsSysTable.class);
        JudgeException.isNull(osSysTable, "输入URL错误");
        return osSysTable;
    }

    public OsSysTable getTableAllInfo(String typeName) {
        // 缓存中读取
        OsSysTable osSysTable = tableCache.getCacheValue(CacheType.tableCache, typeName, OsSysTable.class);
        JudgeException.isNull(osSysTable, "输入URL错误");
        return osSysTable;
    }

    public Map<String, Object> getBaseTableInfo(String tableNames) {
        String[] tableNameArr = tableNames.split(",");
        Map<String, Object> resultMap = new HashMap<>(tableNameArr.length);
        for (String tableName : tableNameArr) {
            // 读取缓存
            val list = tableCache.getCacheValue(CacheType.baseCache, tableName, List.class);
            resultMap.put(tableName, list);
        }
        return resultMap;
    }
}
