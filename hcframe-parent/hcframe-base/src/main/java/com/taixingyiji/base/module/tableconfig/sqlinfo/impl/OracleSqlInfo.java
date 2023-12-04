package com.taixingyiji.base.module.tableconfig.sqlinfo.impl;

import com.taixingyiji.base.module.datasource.utils.DataUnit;
import com.taixingyiji.base.module.tableconfig.dao.TableGenMapper;
import com.taixingyiji.base.module.tableconfig.entity.FieldInfo;
import com.taixingyiji.base.module.tableconfig.entity.TableInfo;
import com.taixingyiji.base.module.tableconfig.sqlinfo.SqlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lhc
 * @description oracle获取信息实现类
 * @date 2020-11-02
 */
@Component
public class OracleSqlInfo implements SqlInfo {

    @Autowired
    TableGenMapper tableGenMapper;

    @Override
    public String getType() {
        return DataUnit.ORACLE;
    }

    @Override
    public String getPrimaryKey(@NonNull String tableName, @Nullable String schema) {
        return tableGenMapper.getOraclePrimaryKey(tableName);
    }

    @Override
    public List<TableInfo> getTableList(@Nullable TableInfo tableInfo, @NonNull String schema) {
        return tableGenMapper.getOracleTableList(tableInfo, schema);
    }

    @Override
    public List<FieldInfo> getFieldList(String schema, String tableName, FieldInfo fieldInfo) {
        return tableGenMapper.getOracleFieldList(tableName);
    }
}
