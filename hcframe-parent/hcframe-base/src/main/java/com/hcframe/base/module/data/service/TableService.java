package com.hcframe.base.module.data.service;

import com.alibaba.fastjson.JSONArray;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface TableService {

    int save(OsSysTable osSysTable, Map<String, Object> map);

    ResultVO<Map<String,Object>> saveWithDate(OsSysTable osSysTable, Map<String, Object> map);

    ResultVO<Integer> update(OsSysTable osSysTable, Map<String, Object> map, Integer version);

    ResultVO<Integer> updateWithDate(OsSysTable tableName, Map<String, Object> map, Integer version);

    ResultVO<Integer> delete(OsSysTable tableName, String ids);

    ResultVO<List<Map<String,Object>>> getListNoPage(OsSysTable tableName, Map<String, Object> map);

    ResultVO<Integer> updateBatch(OsSysTable osSysTable, Map<String, Object> map);

    ResultVO<Integer> updateBatchWithDate(OsSysTable osSysTable, Map<String, Object> map);

    ResultVO<Integer> saveBatch(OsSysTable osSysTable, String map);

    ResultVO<Integer> saveBatchWithDate(OsSysTable osSysTable, String data);

    ResultVO<Map<String,Object>> getBaseTableInfo(String tableNames);

    PageInfo<Map<String,Object>> searchSingleTables(String map, OsSysTable tableName, WebPageInfo webPageInfo);


    PageInfo<Map<String,Object>> searchJoinTables(String data, WebPageInfo webPageInfo, OsSysTable tableName);

    ResultVO<Integer> logicDelete(OsSysTable tableName, String ids);

    Condition.ConditionBuilder getQueryBuilder(JSONArray query, Condition.ConditionBuilder builder);
}
