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
