package com.taixingyiji.base.module.tableconfig.sqlinfo;

import com.taixingyiji.base.module.tableconfig.entity.FieldInfo;
import com.taixingyiji.base.module.tableconfig.entity.TableInfo;

import java.util.List;

/**
 * @author lhc
 * @description Sql信息接口
 * @date 2020-11-02
 */
public interface SqlInfo {

    String getType();

    String getPrimaryKey(String tableName,String schema);

    List<TableInfo> getTableList(TableInfo tableInfo, String schema);

    List<FieldInfo> getFieldList(String schema, String tableName, FieldInfo fieldInfo);

}
