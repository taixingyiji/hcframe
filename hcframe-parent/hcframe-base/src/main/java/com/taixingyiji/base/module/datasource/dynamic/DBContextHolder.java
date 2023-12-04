package com.taixingyiji.base.module.datasource.dynamic;


import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;

/**
 * @author lhc
 * @date 2020-09-23
 * @description 通过ThreadLocal储存当前线程数据源的key
 */
public class DBContextHolder {
    // 对当前线程的操作-线程安全的
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    // 调用此方法，切换数据源
    public static void setDataSource(String dataSource) {
        if (DataSourceUtil.dataSourceMap.containsKey(dataSource)) {
            contextHolder.set(dataSource);
        } else {
            throw new RuntimeException("数据源:" + dataSource + "不存在");
        }
    }

    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }

    // 删除数据源
    public static void clearDataSource() {
        contextHolder.remove();
    }

}
