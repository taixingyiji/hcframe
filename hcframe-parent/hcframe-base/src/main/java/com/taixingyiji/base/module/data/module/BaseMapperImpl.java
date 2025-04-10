package com.taixingyiji.base.module.data.module;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.utils.MyPageHelper;
import com.taixingyiji.base.common.utils.StringUtils;
import com.taixingyiji.base.module.data.dao.TableMapper;
import com.taixingyiji.base.module.data.exception.BaseMapperException;
import com.taixingyiji.base.module.data.exception.SqlException;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import com.taixingyiji.base.module.datasource.utils.DataUnit;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service("base")
public class BaseMapperImpl implements BaseMapper {

    public static final String BASE = "base";
    public static final String TABLE_MAPPER_PACKAGE = "com.taixingyiji.base.module.data.dao.TableMapper.";
    final TableMapper tableMapper;
    final DruidDataSource druidDataSource;

    @Value("${spring.datasource.druid.driver-class-name}")
    public String dataType;
    final SqlSessionTemplate sqlSessionTemplate;

    public BaseMapperImpl(TableMapper tableMapper, SqlSessionTemplate sqlSessionTemplate,
                          DruidDataSource druidDataSource) {
        this.tableMapper = tableMapper;
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.druidDataSource = druidDataSource;
    }

    @Override
    public String getDataConfig() {
        String key;
        DatasourceConfig datasourceConfig = new DatasourceConfig();
        try {

            key = DBContextHolder.getDataSource();
            datasourceConfig = DataSourceUtil.get(key);
        } catch (Exception e) {
            try {
                Connection connection = druidDataSource.getConnection();
                String dbType = connection.getMetaData().getDatabaseProductName();
                if (dbType.contains("Oracle")) {
                    datasourceConfig.setCommonType(DataUnit.ORACLE);
                }
                if (dbType.contains("MySQL")) {
                    datasourceConfig.setCommonType(DataUnit.MYSQL);
                }
                if (dbType.contains("DM")) {
                    datasourceConfig.setCommonType(DataUnit.DAMENG);
                }
                if (dbType.contains("SQLite")) {
                    datasourceConfig.setCommonType(DataUnit.SQLITE);
                }
                if (dbType.contains("PostgreSQL")) {
                    datasourceConfig.setCommonType(DataUnit.HANGO);
                }
                connection.close();
            } catch (Exception e1) {
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
                if (dataType.contains("highgo")) {
                    datasourceConfig.setCommonType(DataUnit.HANGO);
                }
            }
        }
        return datasourceConfig.getCommonType();
    }

    @Override
    public <E> int save(DataMap<E> dataMap) {
        String dataTypeConfig = getDataConfig();
        JudgesNull(dataMap.getData(), "data can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        if (DataUnit.HANGO.equals(dataTypeConfig)) {
            dataMap.getData().putAll(formatMap(dataMap.getData(),dataMap.getTableName()));
        }
        if (StringUtils.isEmpty(dataMap.getPkName())) {
            dataMap.setPkName("ID");
        }
        int i;
        if (DataUnit.ORACLE.equals(dataTypeConfig) || DataUnit.DAMENG.equals(dataTypeConfig) || DataUnit.HANGO.equals(dataTypeConfig)) {
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
        return i;
    }

    public Map<String, Object> formatMap(Map<String, Object> data, String tableName) {
        return getStringObjectMapForHighGo(tableName, data, getDataConfig());
//        // 遍历 Map 并转换值
//        for (Map.Entry<String, Object> entry : data.entrySet()) {
//            Object value = entry.getValue();
//            if (value instanceof String) {
//                String strValue = (String) value;
//                if (strValue.matches("-?\\d+")) { // 匹配整数
//                    data.put(entry.getKey(), Long.parseLong(strValue));
//                } else if (strValue.matches("-?\\d*\\.\\d+")) { // 匹配浮点数
//                    data.put(entry.getKey(), (long) Double.parseDouble(strValue));
//                }
//            } else if (value instanceof Number) {
//                data.put(entry.getKey(), ((Number) value).longValue());
//            }
//        }
    }

    @Override
    public int save(String tableName, String pkName, Map<String, Object> data) {
        JudgesNull(tableName, "data can not be null!");
        JudgesNull(data, "tableName can not be null!");
        String dataTypeConfig = getDataConfig();
        if (DataUnit.HANGO.equals(dataTypeConfig)) {
            data.putAll(formatMap(data, tableName));
        }
        if (StringUtils.isEmpty(pkName)) {
            pkName = "ID";
        }
        int i;
        if (DataUnit.ORACLE.equals(dataTypeConfig) || DataUnit.DAMENG.equals(dataTypeConfig) || DataUnit.HANGO.equals(dataTypeConfig)) {
            if (org.springframework.util.StringUtils.isEmpty(data.get(pkName))) {
                data.put(pkName, getSequence(tableName, pkName));
            }
            i = tableMapper.saveInfoWithOracle(data, tableName);
        } else {
            i = tableMapper.saveInfoWithNull(data, tableName);
            data.put(pkName, data.get("id"));
        }
        SqlException.base(i, "保存失败");
        return i;
    }

    @Override
    public <E> int save(E e) {
        DataMap<E> dataMap = DataMap.<E>builder().obj(e).build();
        int i = save(dataMap);
        dataMap.set(StringUtils.toCamelCase(dataMap.getPkName()), dataMap.getPkValue());
        return i;
    }

    private int updateByWhere(Condition condition, String tableName, Map<String, Object> data) {
        Map<String, Object> params = condition.getParamMap();
        params.put("tableName", tableName);
        String dataTypeConfig = getDataConfig();
        data.putAll(formatMap(data,tableName));
        params.put("info", data);
        params.put("sql", condition.getSql());
        int i = sqlSessionTemplate.update(TABLE_MAPPER_PACKAGE + "updateByWhere", params);
        return i;
    }

    private Map<String, Object> getStringObjectMapForHighGo(String tableName, Map<String, Object> data, String dataTypeConfig) {
        if (DataUnit.HANGO.equals(dataTypeConfig)) {
            // 获取数据库字段类型
            Map<String, String> columnTypes = TableMetadataCache.getColumnTypesFromCache(tableName.toLowerCase());
            if(columnTypes!=null){
                // 自动转换数据类型
                data = DataTypeConverter.convertDataTypes(data, columnTypes);
            }
        }
        return data;
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
        return updateByWhere(condition, dataMap.getTableName(), dataMap.getData());
    }

    @Override
    public int updateByPk(String tableName, String pkName, Map<String, Object> data) {
        JudgesNull(data, "data can not be null!");
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(data.get(pkName), "pkValue can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(pkName, data.get(pkName))
                .build();
        data.remove(pkName);
        return updateByWhere(condition, tableName, data);
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
        return updateByWhere(condition, osSysTable.getTableName(), data);
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
        return updateByWhere(condition, dataMap.getTableName(), dataMap.getData());
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
        return updateByWhere(condition, dataMap.getTableName(), dataMap.getData());
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
        return updateByWhere(condition, tableName, data);
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
        return updateByWhere(condition, osSysTable.getTableName(), data);
    }


    @Override
    public <E> int updateByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getData(), "data can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        return updateByWhere(condition, dataMap.getTableName(), dataMap.getData());
    }

    @Override
    public int updateByCondition(String tableName, Map<String, Object> data, Condition condition) {
        JudgesNull(data, "data can not be null!");
        JudgesNull(tableName, "tableName can not be null!");
        return updateByWhere(condition, tableName, data);
    }

    private int deleteByWhere(Condition condition, String tableName) {
        Map<String, Object> params = condition.getParamMap();
        params.put("tableName", tableName);
        params.put("sql", condition.getSql());
        return sqlSessionTemplate.delete(TABLE_MAPPER_PACKAGE + "deleteByWhere", params);
    }

    @Override
    public <E> int deleteByPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getPkValue(), "pkValue can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(dataMap.getPkName(), dataMap.getPkValue())
                .build();
        return deleteByWhere(condition, dataMap.getTableName());
    }

    @Override
    public int deleteByPk(String tableName, String pkName, Object pkValue) {
        JudgesNull(pkValue, "pkValue can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(tableName, "tableName can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(pkName, pkValue)
                .build();
        return deleteByWhere(condition, tableName);
    }

    @Override
    public int deleteByPk(OsSysTable osSysTable, Object pkValue) {
        JudgesNull(pkValue, "pkValue can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        Condition condition = Condition.creatCriteria()
                .andEqual(osSysTable.getTablePk(), pkValue)
                .build();
        return deleteByWhere(condition, osSysTable.getTableName());
    }

    @Override
    public <E> int deleteInPk(String tableName, String pkName, List<E> ids) {
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(ids, "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(pkName, ids)
                .build();
        return deleteByWhere(condition, tableName);
    }

    @Override
    public <E> int deleteInPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getIds(), "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(dataMap.getPkName(), dataMap.getIdList())
                .build();
        return deleteByWhere(condition, dataMap.getTableName());
    }

    @Override
    public <E> int deleteInPk(OsSysTable osSysTable, List<E> ids) {
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(ids, "ids can not be null!");
        Condition condition = Condition.creatCriteria()
                .andIn(osSysTable.getTablePk(), ids)
                .build();
        return deleteByWhere(condition, osSysTable.getTableName());
    }

    @Override
    public <E> int deleteByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        return deleteByWhere(condition, dataMap.getTableName());
    }

    @Override
    public int deleteByCondition(String tableName, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        return deleteByWhere(condition, tableName);
    }

    @Override
    public List<Map<String, Object>> selectAll(String tableName) {
        JudgesNull(tableName, "tableName can not be null!");
        return tableMapper.useSql(SelectCondition.builder().tableName(tableName).build().getSql());
    }

    private List<Map<String, Object>> selectList(Condition condition, String tableName) {
        Map<String, Object> params = condition.getParamMap();
        String dataTypeConfig = getDataConfig();
        params.put("sql", condition.getSql());
        return sqlSessionTemplate.selectList(TABLE_MAPPER_PACKAGE + "useSql", params);
    }

    private Map<String, Object> selectOne(Condition condition, String tableName) {
        Map<String, Object> params = condition.getParamMap();
        String dataTypeConfig = getDataConfig();
        params.put("sql", condition.getSql());
        return sqlSessionTemplate.selectOne(TABLE_MAPPER_PACKAGE + "userSqlByOne", params);
    }

    @Override
    public <E> List<Map<String, Object>> selectByEqual(DataMap<E> dataMap, Map<String, Object> map) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        Condition condition = equal(dataMap, map);
        return selectList(condition, dataMap.getTableName());
    }

    @Override
    public List<Map<String, Object>> selectByEqual(String tableName, Map<String, Object> map) {
        JudgesNull(tableName, "tableName can not be null!");
        Condition condition = equal(DataMap.builder().tableName(tableName).build(), map);
        return selectList(condition, tableName);
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
        Condition condition = equal(dataMap, map);
        if (webPageInfo.isEnableCache()) {
            return MyPageHelper.start(webPageInfo, condition.getSql(), () -> selectList(condition, dataMap.getTableName()));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(selectList(condition, dataMap.getTableName()));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByEqual(String tableName, Map<String, Object> map, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        Condition condition = equal(DataMap.builder().tableName(tableName).build(), map);
        if (webPageInfo.isEnableCache()) {
            return MyPageHelper.start(webPageInfo, condition.getSql(), () -> selectList(condition, tableName));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(selectList(condition, tableName));
    }

    @Override
    public <E> Map<String, Object> selectOneByEqual(DataMap<E> dataMap, Map<String, Object> map) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        Condition condition = equal(dataMap, map);
        return selectOne(condition, dataMap.getTableName());
    }

    @Override
    public <E> Map<String, Object> selectByPk(DataMap<E> dataMap) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        JudgesNull(dataMap.getPkName(), "pkName can not be null!");
        JudgesNull(dataMap.getPkValue(), "pkValue can not be null!");
        Condition condition = Condition.creatCriteria(dataMap).build();
        condition = condition.toCreatCriteria().andEqual(dataMap.getPkName(), dataMap.getPkValue()).build();
        return selectOne(condition, dataMap.getTableName());
    }

    @Override
    public Map<String, Object> selectByPk(OsSysTable osSysTable, Object pkValue) {
        JudgesNull(osSysTable.getTableName(), "tableName can not be null!");
        JudgesNull(osSysTable.getTablePk(), "pkName can not be null!");
        JudgesNull(pkValue, "pkValue can not be null!");
        Condition condition = Condition.creatCriteria(DataMap.builder().sysOsTable(osSysTable).pkValue(pkValue).build()).build();
        condition = condition.toCreatCriteria().andEqual(osSysTable.getTablePk(), pkValue).build();
        return selectOne(condition, osSysTable.getTableName());
    }

    @Override
    public Map<String, Object> selectByPk(String tableName, String pkName, Object pkValue) {
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        JudgesNull(pkValue, "pkValue can not be null!");
        Condition condition = Condition.creatCriteria(DataMap.builder().tableName(tableName).pkName(pkName).pkValue(pkValue).build()).build();
        condition = condition.toCreatCriteria().andEqual(pkName, pkValue).build();
        return selectOne(condition,tableName);
    }

    @Override
    public List<Map<String, Object>> selectByCondition(Condition condition) {
        JudgesNull(condition.getSelectCondition(), "tableName can not be null!");
        return selectList(condition,condition.getSelectCondition().getTableName());
    }

    @Override
    public <E> List<Map<String, Object>> selectByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        condition = condition.toCreatCriteria(dataMap).build();
        return selectList(condition,dataMap.getTableName());
    }

    @Override
    public List<Map<String, Object>> selectByCondition(String tableName, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).build()).build();
        return selectList(condition,tableName);
    }

    @Override
    public List<Map<String, Object>> selectByCondition(String tableName, List<String> fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition
                .toCreatCriteria(DataMap.builder().tableName(tableName).fieldList(fieldList).build())
                .build();
        return selectList(condition,tableName);
    }

    @Override
    public List<Map<String, Object>> selectByCondition(String tableName, String fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition
                .toCreatCriteria(DataMap.builder().tableName(tableName).fields(fieldList).build())
                .build();
        return selectList(condition,tableName);
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(Condition condition, WebPageInfo webPageInfo) {
        MyPageHelper.start(webPageInfo);
        if (webPageInfo.isEnableCache()) {
            return MyPageHelper.start(webPageInfo, condition.getSql(), () -> selectList(condition,condition.getSelectCondition().getTableName()));
        }
        return new PageInfo<>(selectList(condition,condition.getSelectCondition().getTableName()));
    }

    @Override
    public <E> PageInfo<Map<String, Object>> selectByCondition(DataMap<E> dataMap, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        condition = condition.toCreatCriteria(dataMap).build();
        if (webPageInfo.isEnableCache()) {
            Condition finalCondition = condition;
            return MyPageHelper.start(webPageInfo, condition.getSql(), () -> selectList(finalCondition,dataMap.getTableName()));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(selectList(condition,dataMap.getTableName()));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(String tableName, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).build()).build();
        if (webPageInfo.isEnableCache()) {
            Condition finalCondition = condition;
            return MyPageHelper.start(webPageInfo, condition.getSql(), () -> selectList(finalCondition,tableName));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(selectList(condition,tableName));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(String tableName, List<String> fieldList, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).fieldList(fieldList).build()).build();
        if (webPageInfo.isEnableCache()) {
            Condition finalCondition = condition;
            return MyPageHelper.start(webPageInfo, condition.getSql(), () -> selectList(finalCondition,tableName));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(selectList(condition,tableName));
    }

    @Override
    public PageInfo<Map<String, Object>> selectByCondition(String tableName, String fieldList, Condition condition, WebPageInfo webPageInfo) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).fields(fieldList).build()).build();
        if (webPageInfo.isEnableCache()) {
            Condition finalCondition = condition;
            return MyPageHelper.start(webPageInfo, condition.getSql(), () -> selectList(finalCondition,tableName));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(selectList(condition,tableName));
    }

    @Override
    public <E> Map<String, Object> selectOneByCondition(DataMap<E> dataMap, Condition condition) {
        JudgesNull(dataMap.getTableName(), "tableName can not be null!");
        condition = condition.toCreatCriteria(dataMap).build();
        return selectOne(condition,dataMap.getTableName());
    }

    @Override
    public Map<String, Object> selectOneByCondition(String tableName, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).build()).build();
        return selectOne(condition,tableName);
    }

    @Override
    public Map<String, Object> selectOneByCondition(String tableName, List<String> fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).fieldList(fieldList).build()).build();
        return selectOne(condition,tableName);
    }

    @Override
    public Map<String, Object> selectOneByCondition(String tableName, String fieldList, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        condition = condition.toCreatCriteria(DataMap.builder().tableName(tableName).fields(fieldList).build()).build();
        return selectOne(condition,tableName);
    }

    @Override
    public Map<String, Object> selectOneByCondition(Condition condition) {
        return selectOne(condition,condition.getSelectCondition().getTableName());
    }

    @Override
    public List<Map<String, Object>> selectJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .join(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder,tableName).build().getSql());
    }


    @Override
    public List<Map<String, Object>> selectJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .join(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder,tableName).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectLeftJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .leftJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder,tableName).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectLeftJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .leftJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder,tableName).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectRightJoinByCondition(String tableName, JoinCondition joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .rightJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder,tableName).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectRightJoinByCondition(String tableName, List<JoinCondition> joinCondition, Condition condition) {
        JudgesNull(tableName, "tableName can not be null!");
        SelectCondition selectJoinBuilder = SelectCondition
                .joinBuilder(tableName)
                .rightJoin(joinCondition).build();
        return tableMapper.useSql(condition.toCreatCriteria(selectJoinBuilder,tableName).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectionByCondition(SelectCondition selectCondition, Condition condition) {
        return tableMapper.useSql(condition.toCreatCriteria(selectCondition,selectCondition.getTableName()).build().getSql());
    }

    @Override
    public List<Map<String, Object>> selectSql(String sql) {
        return tableMapper.useSql(sql);
    }

    @Override
    public PageInfo<Map<String, Object>> selectSqlByPage(String sql, WebPageInfo webPageInfo) {
        if (webPageInfo.isEnableCache()) {
            return MyPageHelper.start(webPageInfo, sql, () -> (tableMapper.useSql(sql)));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(tableMapper.useSql(sql));
    }

    @Override
    public List<Map<String, Object>> selectSql(String sql, Map<String, Object> params) {
        params.put("sql", sql);
        return sqlSessionTemplate.selectList(TABLE_MAPPER_PACKAGE + "useSql", params);
    }


    @Override
    public PageInfo<Map<String, Object>> selectSqlByPage(String sql, Map<String, Object> params, WebPageInfo webPageInfo) {
        String dataTypeConfig = getDataConfig();
        params.put("sql", sql);
        if (webPageInfo.isEnableCache()) {
            Map<String, Object> finalParams = params;
            return MyPageHelper.start(webPageInfo, sql, () -> sqlSessionTemplate.selectList(TABLE_MAPPER_PACKAGE + "useSql", finalParams));
        }
        MyPageHelper.start(webPageInfo);
        return new PageInfo<>(sqlSessionTemplate.selectList(TABLE_MAPPER_PACKAGE + "useSql", params));
    }

    @Override
    public Map<String, Object> selectOneSql(String sql) {
        return tableMapper.userSqlByOne(sql);
    }

    @Override
    public Map<String, Object> selectOneSql(String sql, Map<String, Object> params) {
        params.put("sql", sql);
        return sqlSessionTemplate.selectOne(TABLE_MAPPER_PACKAGE + "userSqlByOne", params);
    }

    @Override
    public Long count(String tableName, Condition condition) {
        String sql = condition.getSql();
        sql = "SELECT count(0) as COUNT FROM " + tableName + " " + sql;
        condition.setSql(sql);
        Map<String, Object> map = selectOne(condition, tableName);
        return Long.parseLong(String.valueOf(map.get("COUNT")));
    }

    @Override
    public Long count(Condition condition) {
        return tableMapper.countBySql(condition.getSql());
    }

    @Override
    public int saveBatch(String tableName, String pkName, List<Map<String, Object>> list) {
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        if (list == null || list.isEmpty()) {
            throw new ServiceException("list can not empty");
        }
//        JudgesNull(data, "tableName can not be null!");
        String dataTypeConfig = getDataConfig();
        if (StringUtils.isEmpty(pkName)) {
            pkName = "ID";
        }
        int i;
        if (dataTypeConfig.equals(DataUnit.ORACLE)) {
            for (Map<String, Object> map : list) {
                Object id = getSequence(tableName, pkName);
                map.put(pkName, id);
            }
            i = tableMapper.insertBatch(list, tableName);
        }else if(dataTypeConfig.equals(DataUnit.DAMENG)){
            getSequence(tableName, pkName);
            List<Map<String,Object>> tempList = new ArrayList<>();
            for (Map<String, Object> map : list) {
                map = formatMap(map,tableName);
                map.put(pkName, tableName + "_SEQ.nextval");
                tempList.add(map);
            }
            list = tempList;
            i = tableMapper.insertBatchSeq(list, tableName, pkName);
        } else if (dataTypeConfig.equals(DataUnit.HANGO)) {
            getSequence(tableName, pkName);
            List<Map<String,Object>> tempList = new ArrayList<>();
            for (Map<String, Object> map : list) {
                map = formatMap(map,tableName);
                map.put(pkName, "nextval('" + tableName.toLowerCase() + "_seq')");
                tempList.add(map);
            }
            list = tempList;
            i = tableMapper.insertBatchSeq(list, tableName, pkName);
        } else {
            i = tableMapper.insertBatch(list, tableName);

        }
        SqlException.base(i, "保存失败");
        return i;
    }

    @Override
    public int updateBatchByPk(String tableName, String pkName, List<Map<String, Object>> list) {
        JudgesNull(tableName, "tableName can not be null!");
        JudgesNull(pkName, "pkName can not be null!");
        if (list == null || list.isEmpty()) {
            throw new ServiceException("list can not empty");
        }
        StringBuilder sql = new StringBuilder();
        Map<String, Object> paramMap = new HashMap<>();
        int index = 0;
        for (Map<String, Object> item : list) {
            sql.append("UPDATE ").append(tableName).append(" SET ");
            boolean hasSetClause = false;
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!key.equals(pkName) && value != null && !(value instanceof String && ((String) value).isEmpty())) {
                    sql.append("`").append(key).append("`").append(" = #{item").append(index).append("_").append(key).append("}, ");
                    paramMap.put("item" + index + "_" + key, value);
                    hasSetClause = true;
                }
            }
            if (hasSetClause) {
                // 移除最后的逗号和空格
                sql.setLength(sql.length() - 2);
            } else {
                // 如果没有要更新的字段，跳过这条记录
                continue;
            }
            sql.append(" WHERE ").append("`").append(pkName).append("`").append(" = #{item").append(index).append("_").append(pkName).append("};");
            paramMap.put("item" + index + "_" + pkName, item.get(pkName));
            index++;
        }
        if (index == 0) {
            // 如果没有任何记录需要更新，直接返回
            return 0;
        }
        paramMap.put("sql", sql.toString());
        return this.sqlSessionTemplate.update(TABLE_MAPPER_PACKAGE + "updateBatchByPk", paramMap);
    }

    public void JudgesNull(Object object, String str) {
        if (object == null) {
            throw new BaseMapperException(str);
        }
    }

    public Object getSequence(String tableName, String pkName) {
        String dataTypeConfig = getDataConfig();
        Object id;
        if (DataUnit.HANGO.equals(dataTypeConfig)) {
            if (!tableMapper.judgeHighGoSequenceExist(tableName.toLowerCase())) {
                Map<String, Object> map = selectRecentData(tableName, pkName);
                if (map == null) {
                    tableMapper.createHighGoSequence(tableName.toLowerCase(), 1);
                } else {
                    tableMapper.createHighGoSequence(tableName.toLowerCase(), map.get(pkName));
                }
            }
            id = tableMapper.getHighGoSequence(tableName.toLowerCase());
        } else {
            try {
                String url = druidDataSource.getUrl();
                String schema = getSchemaFromJdbcUrl(url);
                if (tableMapper.judgeDamengSequenceExist(tableName, schema) > 0) {
                    id = tableMapper.getSequence(tableName);
                } else {
                    throw new ServiceException("序列不存在");
                }
            } catch (Exception e) {
                Map<String, Object> map = selectRecentData(tableName, pkName);
                if (map == null) {
                    tableMapper.createSequence(tableName, 1);
                } else {
                    tableMapper.createSequence(tableName, map.get(pkName));
                }
                id = tableMapper.getSequence(tableName);
            }
        }
        return Long.parseLong(id.toString()) + 1L;
    }

    private Map<String, Object> selectRecentData(String tableName, String pkName) {
        MyPageHelper.noCount(WebPageInfo.builder().pageNum(1).pageSize(1).order(WebPageInfo.DESC).sortField(pkName).build());
        DataMap<Object> dataMap = DataMap.builder().tableName(tableName).pkName(pkName).fields(pkName).build();
        Condition condition = Condition.creatCriteria(dataMap).build();
        return selectOneByCondition(condition);
    }

    public static String getSchemaFromJdbcUrl(String url) {
        if (url == null || !url.contains("?")) {
            return null;
        }

        String[] parts = url.split("\\?");
        if (parts.length < 2) {
            return null;
        }

        String[] params = parts[1].split("&");
        for (String param : params) {
            if (param.toLowerCase().startsWith("schema=")) {
                return param.substring("schema=".length());
            }
        }

        return null;
    }
}
