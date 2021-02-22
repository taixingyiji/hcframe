package com.hcframe.base.module.tableconfig.dao;

import com.hcframe.base.module.tableconfig.entity.FieldInfo;
import com.hcframe.base.module.tableconfig.entity.TableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lhc
 * @date 2020-10-20
 * @description 获取数据库信息
 */
public interface TableGenMapper {

    /**
     * 获取Mysql数据表信息
     * @param tableInfo 表信息
     * @param schema schema
     * @return Mysql数据表信息
     */
    List<TableInfo> getMysqlTableList(@Param("tableInfo") TableInfo tableInfo,
                                      @Param("schema") String schema);

    /**
     * 获取Oracle数据表信息
     * @param tableInfo 表信息
     * @param schema schema
     * @return Oracle数据表信息
     */
    List<TableInfo> getOracleTableList(@Param("tableInfo") TableInfo tableInfo,
                                       @Param("schema") String schema);

    /**
     * 获取Mysql字段信息
     * @param schema schema
     * @param tableName 表名
     * @param info 字段信息
     * @return Mysql字段列表
     */
    List<FieldInfo> getMysqlFieldList(@Param("schema") String schema,
                                      @Param("tableName") String tableName,
                                      @Param("info") FieldInfo info);

    /**
     * 获取Oracle字段信息
     * @param tableName 表名
     * @return Oracle字段信息
     */
    List<FieldInfo> getOracleFieldList(@Param("tableName") String tableName);

    /**
     * 获取Oracle主键字段
     * @param tableName 表名
     * @return 主键字段
     */
    String getOraclePrimaryKey(@Param("tableName") String tableName);

    /**
     * 获取Mysql主键字段
     * @param tableName 表名
     * @param schema 库名
     * @return 主键字段
     */
    String getMysqlPrimaryKey(@Param("tableName") String tableName,
                              @Param("schema") String schema);

}
