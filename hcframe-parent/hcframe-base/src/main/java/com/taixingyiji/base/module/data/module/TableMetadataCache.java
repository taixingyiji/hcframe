package com.taixingyiji.base.module.data.module;

import javax.sql.DataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lhc
 * @version 1.0
 * @className TableMetadataCache
 * @date 2025年04月09日 15:46
 * @description 描述
 */
@Component
public class TableMetadataCache {
    // 缓存所有表的字段类型信息
    private static final ConcurrentHashMap<String, Map<String, String>> tableColumnTypesCache = new ConcurrentHashMap<>();
    final DataSource dataSource;

    // 数据库连接池或者数据源

    public TableMetadataCache(DataSource druidDataSource) {
        this.dataSource = druidDataSource;
    }

    // 系统初始化时加载所有表的字段类型
    @PostConstruct
    public void initialize() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                Map<String, String> columnTypes = getColumnTypes(connection, tableName);
                tableColumnTypesCache.put(tableName, columnTypes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取表的字段类型
    private Map<String, String> getColumnTypes(Connection connection, String tableName) throws SQLException {
        Map<String, String> columnTypes = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tableName, null);

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            columnTypes.put(columnName, columnType);
        }

        return columnTypes;
    }

    // 获取缓存中的字段类型
    public static Map<String, String> getColumnTypesFromCache(String tableName) {
        return tableColumnTypesCache.get(tableName);
    }
}
