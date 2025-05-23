package com.taixingyiji.base.module.data.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.utils.JudgeException;
import com.taixingyiji.base.common.utils.TableNameUtil;
import com.taixingyiji.base.module.data.constants.FieldConstants;
import com.taixingyiji.base.module.data.constants.QueryConstants;
import com.taixingyiji.base.module.data.dao.TableMapper;
import com.taixingyiji.base.module.data.module.*;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.data.exception.SqlException;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.base.module.cache.impl.TableCache;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/***
 * @author lhc
 * @description 事务层，主要编写业务逻辑，尽量所有的业务都在事务层编写
 */
@Service
public class TableServiceImpl implements TableService {

    final
    TableMapper tableMapper;

    final
    TableNameUtil tableNameUtil;

    final
    BaseMapper baseMapper;

    final
    TableCache tableCache;

    public TableServiceImpl(TableMapper tableMapper, TableNameUtil tableNameUtil, @Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper, TableCache tableCache) {
        this.tableMapper = tableMapper;
        this.tableNameUtil = tableNameUtil;
        this.baseMapper = baseMapper;
        this.tableCache = tableCache;
    }

    @Override
    public int save(OsSysTable osSysTable, Map<String, Object> map) {
        return baseMapper.save(DataMap.builder().sysOsTable(osSysTable).data(map).build());
    }

    @Override
    public ResultVO<Map<String, Object>> saveWithDate(OsSysTable osSysTable, Map<String, Object> map) {
        map.put(FieldConstants.CREATE_TIME.toString(), new Date());
        map.put(FieldConstants.UPDATE_TIME.toString(), new Date());
        int i = baseMapper.save(DataMap.builder().sysOsTable(osSysTable).data(map).build());
        SqlException.operation(i, "新增数据失败");
        return ResultVO.getSuccess(map);
    }

    @Override
    public ResultVO<Map<String,Object>> update(OsSysTable osSysTable, Map<String, Object> map, Integer version) {
        Object pk = map.get(osSysTable.getTablePk());
        map.remove(osSysTable.getTablePk());
        return versionValid(osSysTable, map, version, pk);
    }

    @Override
    public ResultVO<Map<String,Object>> updateWithDate(OsSysTable osSysTable, Map<String, Object> map, Integer version) {
        Object pk = map.get(osSysTable.getTablePk());
        map.put(FieldConstants.UPDATE_TIME.toString(), new Date());
        map.remove(osSysTable.getTablePk());
        map.remove(FieldConstants.CREATE_TIME.toString());
        return versionValid(osSysTable, map, version, pk);
    }

    public ResultVO<Map<String,Object>> versionValid(OsSysTable osSysTable, Map<String, Object> map, Integer version, Object pk) {
        if (!StringUtils.isEmpty(version)) {
            DataMap dataMap = DataMap.builder().sysOsTable(osSysTable).pkValue(pk).data(map).build();
            Map<String, Object> data = baseMapper.selectByPk(dataMap);
            Integer newVersion = Integer.parseInt(data.get(FieldConstants.VERSION.toString()).toString());
            if (!version.equals(newVersion)) {
                throw new ServiceException("更新失败");
            } else {
                map.put(DataMap.VERSION, version + 1);
            }
        }
        return updateByPk(osSysTable, map, pk);
    }

    public ResultVO<Map<String,Object>> updateByPk(OsSysTable osSysTable, Map<String, Object> map, Object pk) {
        map.remove("ROW_ID");
        // 设置更新项
        DataMap dataMap = DataMap.builder()
                .sysOsTable(osSysTable)
                .pkValue(pk)
                .data(map)
                .build();
        int i = baseMapper.updateByPk(dataMap);
        SqlException.operation(i, "更新失败");
        Map<String,Object> result = baseMapper.selectByPk(osSysTable,pk);
        return ResultVO.getSuccess(result);
    }

    @Override
    public ResultVO<Integer> delete(OsSysTable osSysTable, String ids) {
        JudgeException.isNull(ids, "ids 不能为空");
        int i = baseMapper.deleteInPk(DataMap.builder().sysOsTable(osSysTable).ids(ids).build());
        SqlException.operation(i, "删除失败");
        return ResultVO.getSuccess(i);
    }


    @Override
    public ResultVO<List<Map<String, Object>>> getListNoPage(OsSysTable tableName, Map<String, Object> map) {
        List<Map<String, Object>> list = baseMapper.selectByEqual(DataMap.builder().sysOsTable(tableName).build(), map);
        return ResultVO.getSuccess(list);
    }

    @Override
    public ResultVO<Integer> updateBatch(OsSysTable osSysTable, Map<String, Object> map) {
        String ids = (String) map.get(osSysTable.getTablePk());
        map.remove(osSysTable.getTablePk());
        int i = baseMapper.updateInPk(DataMap.builder().sysOsTable(osSysTable).ids(ids).build());
        SqlException.operation(i, "更新失败");
        return ResultVO.getSuccess(i);
    }

    @Override
    public ResultVO<Integer> updateBatchWithDate(OsSysTable osSysTable, Map<String, Object> map) {
        String ids = (String) map.get(osSysTable.getTablePk());
        map.put(FieldConstants.UPDATE_TIME.toString(), new Date());
        map.remove(osSysTable.getTablePk());
        map.remove(FieldConstants.CREATE_TIME.toString());
        int i = baseMapper.updateInPk(DataMap.builder().sysOsTable(osSysTable).ids(ids).data(map).build());
        SqlException.operation(i, "更新失败");
        return ResultVO.getSuccess(i);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Integer> saveBatch(OsSysTable osSysTable, String map) {
        JSONArray jsonArray = JSONArray.parseArray(map);
        int i = 0;
        for (Object object : jsonArray) {
            Map<String, Object> data = JSON.parseObject(object.toString());
            i = baseMapper.save(DataMap.builder().sysOsTable(osSysTable).data(data).build());
            SqlException.operation(i, "批量新增失败");
        }
        return ResultVO.getSuccess(i);
    }

    @Override
    public ResultVO<Integer> saveBatchWithDate(OsSysTable osSysTable, String data) {
        JSONArray jsonArray = JSONArray.parseArray(data);
        int i = 0;
        for (Object object : jsonArray) {
            Map<String, Object> map = JSON.parseObject(object.toString());
            map.put(FieldConstants.CREATE_TIME.toString(), new Date());
            map.put(FieldConstants.UPDATE_TIME.toString(), new Date());
            i = baseMapper.save(DataMap.builder().sysOsTable(osSysTable).data(map).build());
            SqlException.operation(i, "批量新增失败");
        }
        return ResultVO.getSuccess(i);
    }

    @Override
    public ResultVO<Map<String, Object>> getBaseTableInfo(String tableNames) {
        return ResultVO.getSuccess(tableNameUtil.getBaseTableInfo(tableNames));
    }

    @Override
    public PageInfo<Map<String, Object>> searchSingleTables(String map, OsSysTable tableName, WebPageInfo webPageInfo) {
        return baseMapper.selectByCondition(getMapList(tableName, map).build(), webPageInfo);
    }


    @Override
    public PageInfo<Map<String, Object>> searchJoinTables(String data, WebPageInfo webPageInfo, OsSysTable tableName) {
        if (!StringUtils.isEmpty(data)) {
            try {
                data = URLDecoder.decode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new ServiceException(e);
            }
            JSONObject jsonObject = JSON.parseObject(data);
            JSONArray query = JSON.parseArray(jsonObject.getString(QueryConstants.QUERY));
            JSONArray join = JSON.parseArray(jsonObject.getString(QueryConstants.JOIN));
            SelectCondition.SelectJoinBuilder selectJoinBuilder = SelectCondition.joinBuilder(tableName.getTableName());
            for (Object o : join) {
                JoinCondition joinCondition = JSON.parseObject(String.valueOf(o), JoinCondition.class);
                joinCondition.setName(tableNameUtil.getTableName(joinCondition.getName()).getTableName());
                joinCondition.setFkTable(tableNameUtil.getTableName(joinCondition.getFkTable()).getTableName());
                selectJoinBuilder = selectJoinBuilder.join(joinCondition);
            }
            SelectCondition selectCondition = selectJoinBuilder.build();
            Condition.ConditionBuilder builder = Condition.creatCriteria(selectCondition,selectCondition.getTableName());
            builder = getQueryBuilder(query, builder);
            return baseMapper.selectByCondition(builder.build(), webPageInfo);
        } else {
            throw new ServiceException("参数错误");
        }
    }

    @Override
    public ResultVO<Integer> logicDelete(OsSysTable tableName, String ids) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(FieldConstants.DELETED.toString(), 0);
        map.put(FieldConstants.UPDATE_TIME.toString(), new Date());
        DataMap dataMap = DataMap.builder().sysOsTable(tableName).ids(ids).data(map).build();
        Condition condition = Condition.creatCriteria().andIn(dataMap.getPkName(), dataMap.getIdList()).build();
        int i = baseMapper.updateByCondition(dataMap, condition);
        SqlException.operation(i, "删除失败");
        return ResultVO.getSuccess(i);
    }

    @Override
    public Condition.ConditionBuilder getQueryBuilder(JSONArray query, Condition.ConditionBuilder builder) {
        for (Object qObj : query) {
            WebCondition webCondition = JSON.parseObject(String.valueOf(qObj), WebCondition.class);
            if (!StringUtils.isEmpty(webCondition.getLogic())) {
                builder = WebCondition.setLogic(webCondition, builder);
            } else {
                builder = builder.and();
            }
            if (!StringUtils.isEmpty(webCondition.getCurves())) {
                builder = WebCondition.setCurves(webCondition, builder);
            }
            builder = WebCondition.setSign(webCondition, builder,baseMapper.getDataConfig());
        }
        return builder;
    }

    @Override
    public Map<String, Object> getOne(OsSysTable tableName, String id) {
        Condition condition = Condition.creatCriteria().andEqual("DELETED", 1).andEqual(tableName.getTablePk(), id).build();
        return baseMapper.selectOneByCondition(tableName.getTableName(), condition);
    }

    @Override
    public PageInfo<Map<String, Object>> getReference(OsSysTable tableName, String data, WebPageInfo webPageInfo, String target, String id) {
        if (StringUtils.isEmpty(target)) {
            return new PageInfo<>();
        }
        if (StringUtils.isEmpty(id)) {
            return new PageInfo<>();
        }
        Condition.ConditionBuilder builder = getMapList(tableName, data);
        builder.andEqual(target, id);
        return baseMapper.selectByCondition(builder.build(), webPageInfo);
    }

    @Override
    public List<Map<String, Object>> getMany(OsSysTable tableName, String ids) {
        DataMap dataMap = DataMap.builder().sysOsTable(tableName).ids(ids).build();
        Condition condition = Condition.creatCriteria(dataMap).andIn(dataMap.getPkName(), dataMap.getIdList()).build();
        return baseMapper.selectByCondition(condition);
    }

    public Condition.ConditionBuilder getMapList(OsSysTable tableName, String data) {
        DataMap dataMap = DataMap.builder().sysOsTable(tableName).build();
        Condition.ConditionBuilder builder = Condition.creatCriteria(dataMap);
        if (!StringUtils.isEmpty(data)) {
            try {
                data = URLDecoder.decode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new ServiceException(e);
            }
            JSONArray jsonArray = JSON.parseArray(data);
            builder = getQueryBuilder(jsonArray, builder);
        }
        builder.andEqual("DELETED", 1);
        return builder;
    }

    public static void main(String[] args) {
        System.out.println(SelectCondition.builder().tableName("table").build().getSql());
    }

}
