package com.taixingyiji.base.module.data.module;

import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 */
public interface BaseMapper {

    <E> int save(DataMap<E> dataMap);
    String getDataConfig();
    int save(String tableName, String pkName, Map<String, Object> data);

    <E> int save(E e);

    <E> int updateByPk(DataMap<E> dataMap);

    int updateByPk(String tableName, String pkName, Map<String, Object> data);

    int updateByPk(OsSysTable osSysTable, Map<String, Object> data);

    <E> int updateByPk(E e);

    <E> int updateInPk(DataMap<E> dataMap);

    <E, A> int updateInPk(E e, List<A> ids);

    <E> int updateInPk(String tableName, String pkName, List<E> ids, Map<String, Object> data);

    <E> int updateInPk(OsSysTable osSysTable, List<E> ids, Map<String, Object> data);

    <E> int updateByCondition(DataMap<E> dataMap, Condition condition);

    int updateByCondition(String tableName, Map<String, Object> data, Condition condition);

    <E> int deleteByPk(DataMap<E> dataMap);

    int deleteByPk(String tableName, String pkName, Object pkValue);

    int deleteByPk(OsSysTable osSysTable, Object pkValue);

    <E> int deleteInPk(String tableName, String pkName, List<E> ids);

    <E> int deleteInPk(DataMap<E> dataMap);

    <E> int deleteInPk(OsSysTable osSysTable, List<E> ids);

    <E> int deleteByCondition(DataMap<E> dataMap, Condition condition);

    int deleteByCondition(String tableName, Condition condition);

    List<Map<String, Object>> selectAll(String tableName);

    <E> List<Map<String, Object>> selectByEqual(DataMap<E> dataMap, Map<String, Object> map);

    List<Map<String, Object>> selectByEqual(String tableName, Map<String, Object> map);

    <E> PageInfo<Map<String, Object>> selectByEqual(DataMap<E> dataMap, Map<String, Object> map, WebPageInfo webPageInfo);

    PageInfo<Map<String, Object>> selectByEqual(String tableName, Map<String, Object> map, WebPageInfo webPageInfo);

    <E> Map<String, Object> selectOneByEqual(DataMap<E> dataMap, Map<String, Object> map);

    <E> Map<String, Object> selectByPk(DataMap<E> dataMap);

    Map<String, Object> selectByPk(OsSysTable osSysTable, Object pkValue);

    Map<String, Object> selectByPk(String tableName, String pkName, Object pkValue);

    List<Map<String, Object>> selectByCondition(Condition condition);

    <E> List<Map<String, Object>> selectByCondition(DataMap<E> dataMap, Condition condition);

    List<Map<String, Object>> selectByCondition(String tableName, Condition condition);

    List<Map<String, Object>> selectByCondition(String tableName, List<String> fieldList, Condition condition);

    List<Map<String, Object>> selectByCondition(String tableName, String fieldList, Condition condition);

    PageInfo<Map<String, Object>> selectByCondition(Condition condition, WebPageInfo webPageInfo);
    List<Map<String, Object>> selectByConditionAllKey(String tableName, Condition condition);
    PageInfo<Map<String, Object>> selectByConditionAllKey(String tableName, Condition condition, WebPageInfo webPageInfo);
    <E> PageInfo<Map<String, Object>> selectByCondition(DataMap<E> dataMap, Condition condition, WebPageInfo webPageInfo);

    PageInfo<Map<String, Object>> selectByCondition(String tableName, Condition condition, WebPageInfo webPageInfo);

    PageInfo<Map<String, Object>> selectByCondition(String tableName, List<String> fieldList, Condition condition, WebPageInfo webPageInfo);

    PageInfo<Map<String, Object>> selectByCondition(String tableName, String fieldList, Condition condition, WebPageInfo webPageInfo);

    <E> Map<String, Object> selectOneByCondition(DataMap<E> dataMap, Condition condition);

    Map<String, Object> selectOneByCondition(String tableName, Condition condition);

    Map<String, Object> selectOneByCondition(String tableName, List<String> fieldList, Condition condition);

    Map<String, Object> selectOneByCondition(String tableName, String fieldList, Condition condition);

    Map<String, Object> selectOneByCondition(Condition condition);

    List<Map<String, Object>> selectJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition);

    List<Map<String, Object>> selectJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition);

    List<Map<String, Object>> selectLeftJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition);

    List<Map<String, Object>> selectLeftJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition);

    List<Map<String, Object>> selectRightJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition);

    List<Map<String, Object>> selectRightJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition);

    List<Map<String, Object>> selectionByCondition(SelectCondition selectCondition, Condition condition);

    List<Map<String, Object>> selectSql(String sql);

    /**
     * 执行单条非查询 SQL（DML 或 DDL），返回 JDBC 更新计数；DDL 的计数由驱动决定。
     * 查询 SQL 请使用 selectSql / selectOneSql。
     */
    int executeSql(String sql);

    /**
     * 执行单条非查询 SQL，使用 #{name} 绑定 params 中的值，不修改传入的 Map。
     * params 可以为 null；sql 为内部保留参数名。SQL 结构应由调用方提供可信内容。
     */
    int executeSql(String sql, Map<String, Object> params);

    /**
     * executeSql 的兼容拼写。
     */
    default int excuteSql(String sql) {
        return executeSql(sql);
    }

    /**
     * executeSql 的兼容拼写，参数规则相同。
     */
    default int excuteSql(String sql, Map<String, Object> params) {
        return executeSql(sql, params);
    }

    /** 插入 SQL，返回受影响行数，不返回生成的主键。 */
    int insertSql(String sql);

    /** 插入 SQL，参数规则同 executeSql，返回受影响行数。 */
    int insertSql(String sql, Map<String, Object> params);

    /** 更新 SQL，返回受影响行数，未匹配记录时返回 0。 */
    int updateSql(String sql);

    /** 更新 SQL，参数规则同 executeSql，返回受影响行数。 */
    int updateSql(String sql, Map<String, Object> params);

    /** 删除 SQL，返回受影响行数，未匹配记录时返回 0。 */
    int deleteSql(String sql);

    /** 删除 SQL，参数规则同 executeSql，返回受影响行数。 */
    int deleteSql(String sql, Map<String, Object> params);

    /** 执行调用方提供的 COUNT 查询，SQL 应返回单行单列的计数值。 */
    Long countSql(String sql);

    /** 执行 COUNT 查询，参数规则同 executeSql，不自动包装 SQL。 */
    Long countSql(String sql, Map<String, Object> params);

    PageInfo<Map<String, Object>> selectSqlByPage(String sql, WebPageInfo webPageInfo);
    List<Map<String, Object>> selectSql(String sql,Map<String,Object> params);

    PageInfo<Map<String, Object>> selectSqlByPage(String sql,Map<String,Object> params, WebPageInfo webPageInfo);

    Map<String, Object> selectOneSql(String sql);
    Map<String, Object> selectOneSql(String sql,Map<String,Object> params);
    Long count(String tableName, Condition condition);

    Long count(Condition condition);
    int saveBatch(String tableName,String pkName, List<Map<String,Object>> list);

    int updateBatchByPk(String tableName,String pkName,List<Map<String,Object>> list);

}
