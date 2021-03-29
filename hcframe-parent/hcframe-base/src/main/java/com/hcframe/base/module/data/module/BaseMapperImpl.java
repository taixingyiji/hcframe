package com.hcframe.base.module.data.module;

import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.MyPageHelper;
import com.hcframe.base.common.utils.StringUtils;
import com.hcframe.base.module.data.dao.TableMapper;
import com.hcframe.base.module.data.exception.BaseMapperException;
import com.hcframe.base.module.data.exception.SqlException;
import com.hcframe.base.module.datasource.dynamic.DBContextHolder;
import com.hcframe.base.module.datasource.entity.DatasourceConfig;
import com.hcframe.base.module.datasource.utils.DataSourceUtil;
import com.hcframe.base.module.datasource.utils.DataUnit;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("base")
public class BaseMapperImpl implements BaseMapper {

    public static final String BASE = "base";

    final TableMapper tableMapper;

    @Value("${spring.datasource.druid.driver-class-name}")
    public String dataType;

    public BaseMapperImpl(TableMapper tableMapper) {
        this.tableMapper = tableMapper;
    }

    @Override
    @Transactional
    public  <E> int save(DataMap<E> dataMap) {
        String key;
        DatasourceConfig datasourceConfig = new DatasourceConfig();
        try {
            key = DBContextHolder.getDataSource();
            datasourceConfig = DataSourceUtil.get(key);
        } catch (Exception e) {
            if (dataType.contains("oracle")) {
                datasourceConfig.setCommonType(DataUnit.ORACLE);
            }
            if (dataType.contains("mysql")) {
                datasourceConfig.setCommonType(DataUnit.MYSQL);
            }
            if (dataType.contains("DmDriver")) {
                datasourceConfig.setCommonType(DataUnit.DAMENG);
            }
            if (dataType.contains("sqlite")) {
                datasourceConfig.setCommonType(DataUnit.SQLITE);
            }
        }
        JudgesNull(dataMap.getData(), "data can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        if (StringUtils.isEmpty(dataMap.getPkName())) {
            dataMap.setPkName("ID");
        }
        int i;
        if (DataUnit.ORACLE.equals(datasourceConfig.getCommonType()) || DataUnit.DAMENG.equals(datasourceConfig.getCommonType())) {
            if (org.springframework.util.StringUtils.isEmpty(dataMap.get(dataMap.getPkName()))) {
                Object id = getSequence(dataMap.getTableName(), dataMap.getPkName());
                dataMap.toBuilder().add(dataMap.getPkName(), id);
                dataMap.setPkValue(id);
            }
            i = tableMapper.saveInfoWithOracle(dataMap.getData(), dataMap.getTableName());
        } else {
            i = tableMapper.saveInfoWithNull(dataMap.getData(), dataMap.getTableName());
            Object id = dataMap.getData().get("id");
            dataMap.toBuilder().add(dataMap.getPkName(), id).build();
            dataMap.setPkValue(id);
        }
        SqlException.base(i, "保存失败");
        dataMap.toBuilder().remove("id");
        return i;
    }

    @Override
    @Transactional
    public int save(String tableName, String pkName, Map<String, Object> data) {
        JudgesNull(tableName, "data can not be null!");
        JudgesNull(data, "tableName can not be null!");
        String key;
        DatasourceConfig datasourceConfig = new DatasourceConfig();
        try {
            key = DBContextHolder.getDataSource();
            datasourceConfig = DataSourceUtil.get(key);
        } catch (Exception e) {
            if (dataType.contains("oracle")) {
                datasourceConfig.setCommonType(DataUnit.ORACLE);
            }
            if (dataType.contains("mysql")) {
                datasourceConfig.setCommonType(DataUnit.MYSQL);
            }
            if (dataType.contains("DmDriver")) {
                datasourceConfig.setCommonType(DataUnit.DAMENG);
            }
            if (dataType.contains("sqlite")) {
                datasourceConfig.setCommonType(DataUnit.SQLITE);
            }
        }
        if (StringUtils.isEmpty(pkName)) {
            pkName = "ID";
        }
        int i;
        if (datasourceConfig.getCommonType().equals(DataUnit.ORACLE)||datasourceConfig.getCommonType().equals(DataUnit.DAMENG)) {
            if (org.springframework.util.StringUtils.isEmpty(data.get(pkName))) {
                data.put(pkName, getSequence(tableName, pkName));
            }
            i = tableMapper.saveInfoWithOracle(data, tableName);
        } else {
            i = tableMapper.saveInfoWithNull(data, tableName);
            data.put(pkName, data.get("id"));
        }
        SqlException.base(i, "保存失败");
        data.remove("id");
        return i;
    }

    @Override
    public <E> int save(E e) {
        DataMap<E> dataMap = DataMap.<E>builder().obj(e).build();
        int i = save(dataMap);
        dataMap.set(StringUtils.toCamelCase(dataMap.getPkName()),dataMap.getPkValue());
        return i;
    }


    @Override
    public <E> int updateByPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getData(), "data can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getPkValue(), "pkValue can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(dataMap.getPkName(), dataMap.getPkValue())
                .build();
        int i = tableMapper.updateByWhere(dataMap.getData(), dataMap.getTableName(), condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }

    @Override
    public int updateByPk(String tableName, String pkName,  Map<String, Object> data) {
        JudgesNull(data, "data can not be null!");
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(data.get(pkName), "pkValue can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(pkName, data.get(pkName))
                .build();
        data.remove(pkName);
        int i = tableMapper.updateByWhere(data, tableName, condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }

    @Override
    public int updateByPk(OsSysTable osSysTable, Map<String, Object> data) {
        JudgesNull(data, "data can not be null!");
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(data.get(osSysTable.getTablePk()), "pkValue can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(osSysTable.getTablePk(), data.get(osSysTable.getTablePk()))
                .build();
        int i = tableMapper.updateByWhere(data, osSysTable.getTableName(), condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }

    @Override
    public <E> int updateByPk(E e) {
        return updateByPk(DataMap.<E>builder().obj(e).build());
    }

    @Override
    public <E, A> int updateInPk(E e, List<A> ids) {
        DataMap<E> dataMap = DataMap.<E>builder().obj(e).build();
        Condition condition = Condition.creatCriteria()
                .andIn(dataMap.getPkName(), ids)
                .build();
        int i = tableMapper.updateByWhere(dataMap.getData(), dataMap.getTableName(), condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }


    @Override
    public <E> int updateInPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getData(), "data can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getIds(), "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(dataMap.getPkName(), dataMap.getIdList())
                .build();
        int i = tableMapper.updateByWhere(dataMap.getData(), dataMap.getTableName(), condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }

    @Override
    public <E> int updateInPk(String tableName, String pkName, List<E> ids, Map<String, Object> data) {
        JudgesNull(data, "data can not be null!");
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(ids, "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(pkName, ids)
                .build();
        int i = tableMapper.updateByWhere(data, tableName, condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }

    @Override
    public <E> int updateInPk(OsSysTable osSysTable, List<E> ids, Map<String, Object> data) {
        JudgesNull(data, "data can not be null!");
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(ids, "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(osSysTable.getTablePk(), ids)
                .build();
        int i = tableMapper.updateByWhere(data, osSysTable.getTableName(), condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }


    @Override
    public <E> int updateByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getData(), "data can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        int i = tableMapper.updateByWhere(dataMap.getData(), dataMap.getTableName(), condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }

    @Override
    public int updateByCondition(String tableName, Map<String, Object> data, Condition condition) {
        JudgesNull(data, "data can not be null!");
        JudgesNull(tableName, "tableName can not be null!");
        int i = tableMapper.updateByWhere(data, tableName, condition.getSql());
        SqlException.base(i, "更新失败");
        return i;
    }

    @Override
    public <E> int deleteByPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getPkValue(), "pkValue can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(dataMap.getPkName(), dataMap.getPkValue())
                .build();
        int i = tableMapper.deleteByWhere(dataMap.getTableName(), condition.getSql());
        return i;
    }

    @Override
    public int deleteByPk(String tableName, String pkName, Object pkValue) {
        JudgesNull(pkValue, "pkValue can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(tableName, "tableName can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(pkName, pkValue)
                .build();
        int i = tableMapper.deleteByWhere(tableName, condition.getSql());
        return i;
    }

    @Override
    public int deleteByPk(OsSysTable osSysTable, Object pkValue) {
        JudgesNull(pkValue, "pkValue can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(osSysTable.getTablePk(), pkValue)
                .build();
        int i = tableMapper.deleteByWhere(osSysTable.getTableName(), condition.getSql());
        return i;
    }

    @Override
    public <E> int deleteInPk(String tableName, String pkName, List<E> ids) {
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(ids, "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(pkName, ids)
                .build();
        int i = tableMapper.deleteByWhere(tableName, condition.getSql());
        return i;
    }

    @Override
    public <E> int deleteInPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getIds(), "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(dataMap.getPkName(), dataMap.getIdList())
                .build();
        int i = tableMapper.deleteByWhere(dataMap.getTableName(), condition.getSql());
        return i;
    }

    @Override
    public <E> int deleteInPk(OsSysTable osSysTable, List<E> ids) {
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(ids, "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(osSysTable.getTablePk(), ids)
                .build();
        int i = tableMapper.deleteByWhere(osSysTable.getTableName(), condition.getSql());
        return i;
    }

    @Override
    public <E> int deleteByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        int i = tableMapper.deleteByWhere(dataMap.getTableName(), condition.getSql());
        return i;
    }

    @Override
    public int deleteByCondition(String tableName, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        int i = tableMapper.deleteByWhere(tableName, condition.getSql());
        return i;
    }

    @Override
    public List<Map<String, Object>> selectAll(String tableName) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.useSql(SelectCondition.builder().tableName(tableName).build().getSql());
    }

    @Override
    public <E> List<Map<String, Object>> selectByEqual(DataMap<E> dataMap, Map<String, Object> map) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        Condition condition = equal(dataMap, map);
        return tableMapper.useSql(condition.getSql());
    }

    @Override
    public List<Map<String, Object>> selectByEqual(String tableName, Map<String, Object> map) {
        JudgesNull(tableName, "tableName can not be null!");
        Condition condition = equal(DataMap.builder().tableName(tableName).build(), map);
        return tableMapper.useSql(condition.getSql());
    }


    private <E> Condition equal(DataMap<E> dataMap, Map<String, Object> map) {
        Condition condition = Condition.creatCriteria(dataMap).build();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                condition = condition
                        .toCreatCriteria()
                        .andEqual(entry.getKey(), entry.getValue())
                        .build();
            }
        }
        return condition;
    }

    @Override
    public <E> PageInfo<Map<String, Object>> selectByEqual(DataMap<E> dataMap, Map<String, Object> map, WebPageInfo webPageInfo) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        MyPageHelper.start(webPageInfo);
        Condition condition = equal(dataMap, map);
        return new PageInfo<>(tableMapper.useSql(condition.getSql()));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByEqual(String tableName, Map<String, Object> map, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        MyPageHelper.start(webPageInfo);
        Condition condition = equal(DataMap.builder().tableName(tableName).build(), map);
        return new PageInfo<>(tableMapper.useSql(condition.getSql()));
    }

    @Override
    public <E> Map<String, Object> selectOneByEqual(DataMap<E> dataMap, Map<String, Object> map) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        Condition condition = equal(dataMap, map);
        return tableMapper.userSqlByOne(condition.getSql());
    }

    @Override
    public <E> Map<String, Object> selectByPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getPkValue(), "pkValue can not be null!");
        Condition condition = Condition.creatCriteria(dataMap).build();
        condition = condition.toCreatCriteria().andEqual(dataMap.getPkName(), dataMap.getPkValue()).build();
        return tableMapper.userSqlByOne(condition.getSql());
    }

    @Override
    public Map<String, Object> selectByPk(OsSysTable osSysTable, Object pkValue) {
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(pkValue, "pkValue can not be null!");
        Condition condition = Condition.creatCriteria(DataMap.builder().sysOsTable(osSysTable).pkValue(pkValue).build()).build();
        condition = condition.toCreatCriteria().andEqual(osSysTable.getTablePk(), pkValue).build();
        return tableMapper.userSqlByOne(condition.getSql());
    }

    @Override
    public Map<String, Object> selectByPk(String tableName, String pkName, Object pkValue) {
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(pkValue, "pkValue can not be null!");
        Condition condition = Condition.creatCriteria(DataMap.builder().tableName(tableName).pkName(pkName).pkValue(pkValue).build()).build();
        condition = condition.toCreatCriteria().andEqual(pkName, pkValue).build();
        return tableMapper.userSqlByOne(condition.getSql());
    }

    @Override
    public List<Map<String, Object>> selectByCondition(Condition condition) {
        return tableMapper.useSql(condition.getSql());
    }

    @Override
    public <E> List<Map<String, Object>> selectByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        return tableMapper.useSql(condition.toCreatCriteria(dataMap).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectByCondition(String tableName, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.useSql(condition.toCreatCriteria(DataMap.builder().tableName(tableName).build()).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectByCondition(String tableName, List<String> fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.useSql(condition
                .toCreatCriteria(DataMap.builder().tableName(tableName).fieldList(fieldList).build())
                .build()
                .getSql());
    }

    @Override
    public List<Map<String, Object>> selectByCondition(String tableName, String fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.useSql(condition
                .toCreatCriteria(DataMap.builder().tableName(tableName).fields(fieldList).build())
                .build()
                .getSql());
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(Condition condition, WebPageInfo webPageInfo) {
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(tableMapper.useSql(condition.getSql()));
    }

    @Override
    public <E> PageInfo<Map<String, Object>> selectByCondition(DataMap<E> dataMap, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        MyPageHelper.start(webPageInfo);
        condition = condition.toCreatCriteria(dataMap).build();
        return new PageInfo<>(tableMapper.useSql(condition.getSql()));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(String tableName, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        MyPageHelper.start(webPageInfo);
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).build()).build();
        return new PageInfo<>(tableMapper.useSql(condition.getSql()));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(String tableName, List<String> fieldList, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        MyPageHelper.start(webPageInfo);
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).fieldList(fieldList).build()).build();
        return new PageInfo<>(tableMapper.useSql(condition.getSql()));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(String tableName, String fieldList, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        MyPageHelper.start(webPageInfo);
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).fields(fieldList).build()).build();
        return new PageInfo<>(tableMapper.useSql(condition.getSql()));
    }

    @Override
    public <E> Map<String, Object> selectOneByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        return tableMapper.userSqlByOne(condition.toCreatCriteria(dataMap).build().getSql());
    }

    @Override
    public Map<String, Object> selectOneByCondition(String tableName, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.userSqlByOne(condition.toCreatCriteria(DataMap.builder().tableName(tableName).build()).build().getSql());
    }

    @Override
    public Map<String, Object> selectOneByCondition(String tableName, List<String> fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.userSqlByOne(condition.toCreatCriteria(DataMap.builder().tableName(tableName).fieldList(fieldList).build()).build().getSql());
    }

    @Override
    public Map<String, Object> selectOneByCondition(String tableName, String fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.userSqlByOne(condition.toCreatCriteria(DataMap.builder().tableName(tableName).fields(fieldList).build()).build().getSql());
    }

    @Override
    public Map<String, Object> selectOneByCondition(Condition condition) {
        return tableMapper.userSqlByOne(condition.getSql());
    }

    @Override
    public List<Map<String, Object>> selectJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .join(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .join(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectLeftJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .leftJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectLeftJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .leftJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectRightJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .rightJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectRightJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .rightJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectionByCondition(SelectCondition selectCondition, Condition condition) {
        return tableMapper.useSql(condition.toCreatCriteria(selectCondition).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectSql(String sql) {
        return tableMapper.useSql(sql);
    }

    @Override
    public PageInfo<Map<String, Object>> selectSqlByPage(String sql, WebPageInfo webPageInfo) {
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(tableMapper.useSql(sql));
    }

    @Override
    public Map<String, Object> selectOneSql(String sql) {
        return tableMapper.userSqlByOne(sql);
    }

    public void JudgesNull(Object object, String str) {
        if (object == null) {
            throw new BaseMapperException(str);
        }
    }

    private Object getSequence(String tableName, String pkName) {
        Object id;
        try {
            id = tableMapper.getSequence(tableName);
        } catch (Exception e) {
            MyPageHelper.start(WebPageInfo.builder().pageNum(1).pageSize(1).order(WebPageInfo.DESC).sortField(pkName).build());
            DataMap<Object> dataMap = DataMap.builder().tableName(tableName).pkName(pkName).fields(pkName).build();
            Condition condition = Condition.creatCriteria(dataMap).build();
            Map<String, Object> map = selectOneByCondition(condition);
            if (map == null) {
                tableMapper.createSequence(tableName, 1);
            } else {
                tableMapper.createSequence(tableName, map.get(pkName));
            }
            id = tableMapper.getSequence(tableName);
        }
        return Long.parseLong(id.toString())+1L;
    }
}
