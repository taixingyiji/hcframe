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
 * @description Mysql获取信息实现类
 * @date 2020-11-02
 */
@Component
public class MysqlSqlInfo implements SqlInfo {

    @Autowired
    TableGenMapper tableGenMapper;

    @Override
    public String getType() {
        return DataUnit.MYSQL;
    }

    @Override
    public String getPrimaryKey(@NonNull String tableName,@NonNull String schema) {
        return tableGenMapper.getMysqlPrimaryKey(tableName, schema);
    }

    @Override
    public List<TableInfo> getTableList(@Nullable TableInfo tableInfo, @NonNull String schema) {
        return tableGenMapper.getMysqlTableList(tableInfo, schema);
    }

    @Override
    public List<FieldInfo> getFieldList(@NonNull String schema, @NonNull String tableName, @Nullable FieldInfo fieldInfo) {
        return tableGenMapper.getMysqlFieldList(schema,tableName,fieldInfo);
    }
}
