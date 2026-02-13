package com.taixingyiji.base.module.datasource.dynamic;

import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 使用 ConcurrentHashMap 储存当前线程数据源的 key，完全避免 ThreadLocal
 */
public class DBContextHolder {
    private static final ConcurrentHashMap<Long, String> contextHolder = new ConcurrentHashMap<>();

    // 切换数据源
    public static void setDataSource(String dataSource) {
        Long threadId = Thread.currentThread().getId();
        if (DataSourceUtil.dataSourceMap.containsKey(dataSource)) {
            contextHolder.put(threadId, dataSource);
        } else {
            throw new RuntimeException("数据源:" + dataSource + "不存在");
        }
    }

    // 获取数据源
    public static String getDataSource() {
        Long threadId = Thread.currentThread().getId();
        return contextHolder.get(threadId);
    }

    // 清理线程数据
    public static void clearDataSource() {
        Long threadId = Thread.currentThread().getId();
        contextHolder.remove(threadId);
    }
}