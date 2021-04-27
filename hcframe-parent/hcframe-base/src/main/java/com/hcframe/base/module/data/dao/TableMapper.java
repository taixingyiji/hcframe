package com.hcframe.base.module.data.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TableMapper {

    List<Map<String ,Object>> findAllTables(@Param("tableName") String tableName);

    List<Map<String, Object>> findAllTablesWithField(@Param("fields") String fields, @Param("tableName") String tableName);

    List<Map<String ,Object>> findByEqual(@Param("tableName") String tableName, @Param("info") Map<String, Object> map);

    List<Map<String ,Object>> findByLike(@Param("tableName") String tableName, @Param("info") Map<String, Object> map);

    int deleteByPrimary(@Param("ids") String[] id, @Param("tableName") String tableName, @Param("pkName") String pkName);

    int saveInfo(@Param("info") Map<String, Object> map, @Param("tableName") String tableName);

    int saveInfoWithNull(@Param("info") Map<String, Object> map, @Param("tableName") String tableName);

    int saveInfoWithOracle(@Param("info") Map<String, Object> map, @Param("tableName") String tableName);

    int updateInfo(@Param("info") Map<String, Object> map, @Param("tableName") String tableName);

    int updateInfoWithNull(@Param("info") Map<String, Object> map, @Param("tableName") String tableName);

    int updateByWhere(@Param("info") Map<String, Object> map, @Param("tableName") String tableName, @Param("sql") String sql);

    int updateByWhereWithNull(@Param("info") Map<String, Object> map, @Param("tableName") String tableName, @Param("sql") String sql);

    int deleteByWhere(@Param("tableName") String tableName, @Param("sql") String sql);

    List<Map<String, Object>> useSql(@Param("sql") String sql);

    Map<String, Object> userSqlByOne(@Param("sql") String sql);

    Object useSqlByTest(@Param("sql") String sql);

    List<Map<String, Object>> getListNoPage(@Param("typeName") String tableName, @Param("info") Map<String, Object> map);

    Object getSequence(@Param("tableName") String tableName);

    int createSequence(@Param("tableName") String tableName, @Param("lastId")Object lastId);

    Long count(@Param("tableName")String tableName,@Param("sql")String sql);

    Long countBySql(@Param("sql") String sql);
}
