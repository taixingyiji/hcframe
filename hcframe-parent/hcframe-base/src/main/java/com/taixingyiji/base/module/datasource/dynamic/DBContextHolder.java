package com.taixingyiji.base.module.datasource.dynamic;

import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 使用 ConcurrentHashMap 储存当前线程数据源的 key，兼容性更好
 */
public class DBContextHolder {
    private static final ConcurrentHashMap<Long, String> contextHolder = new ConcurrentHashMap<>();
    private static final ThreadLocal<Long> threadIdHolder = ThreadLocal.withInitial(() -> Thread.currentThread().getId());

    // 切换数据源
    public static void setDataSource(String dataSource) {
        if (DataSourceUtil.dataSourceMap.containsKey(dataSource)) {
            contextHolder.put(threadIdHolder.get(), dataSource);
        } else {
            throw new RuntimeException("数据源:" + dataSource + "不存在");
        }
    }

    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get(threadIdHolder.get());
    }

    // 清理线程数据
    public static void clearDataSource() {
        contextHolder.remove(threadIdHolder.get());
    }
}