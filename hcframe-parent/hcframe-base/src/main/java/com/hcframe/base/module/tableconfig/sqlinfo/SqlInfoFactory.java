package com.hcframe.base.module.tableconfig.sqlinfo;

import com.hcframe.base.common.utils.SpringContextUtil;
import com.hcframe.base.module.tableconfig.sqlinfo.impl.MysqlSqlInfo;
import com.hcframe.base.module.tableconfig.sqlinfo.impl.OracleSqlInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lhc
 * @description SqlInfo工厂
 * @date 2020-11-02
 */
public class SqlInfoFactory {

    private final Map<String, SqlInfo> map;

    public SqlInfoFactory(){
        // 将工厂实现类加入到列表中
        List<SqlInfo> sqlInfos = new ArrayList<>();
        sqlInfos.add((SqlInfo) SpringContextUtil.getBean(OracleSqlInfo.class));
        sqlInfos.add((SqlInfo) SpringContextUtil.getBean(MysqlSqlInfo.class));
        // 将列表信息转为Map
        map = sqlInfos
                .stream()
                .collect(Collectors.toMap(SqlInfo::getType, Function.identity(),((existing, replacement) -> existing) ,ConcurrentHashMap::new));
    }

    public static class Holder {
        public static SqlInfoFactory instance = new SqlInfoFactory();
    }

    public static SqlInfoFactory getInstance() {
        return Holder.instance;
    }

    public SqlInfo get(String type) {
        return map.get(type);
    }

}
