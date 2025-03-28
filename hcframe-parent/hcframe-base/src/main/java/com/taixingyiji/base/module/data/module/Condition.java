package com.taixingyiji.base.module.data.module;

import cn.hutool.core.util.IdUtil;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.utils.XssClass;
import com.taixingyiji.base.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @decription 生成where条件sqlApi
 * @date 2020-12-22
 */
public class Condition implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(Condition.class);

    private static final long serialVersionUID = -1097188960881519597L;
    public static String L_CURVES = "(";
    public static String R_CURVES = ")";
    public static String EQUAL = "=";
    public static String WHERE = "WHERE";
    public static String OneEq = "1=1";
    public static String LIKE = "LIKE";
    public static String IN = "IN";
    public static String AND = "AND";
    public static String OR = "OR";
    public static String NOT_EQUAL = "!=";
    public static String NOT = "NOT";
    public static String BETWEEN = "BETWEEN";
    public static String LT = "<";
    public static String GT = ">";
    public static String LTE = "<=";
    public static String GTE = ">=";
    public static String GROUP_BY = "GROUP BY";
    private Map<String, Object> paramMap = new LinkedHashMap<>();


    private String sql = "";

    private String selecSql = "";

    private String conditionSql = "";

    private SelectCondition selectCondition;

    public Condition() {
    }

    public Condition(String sql, SelectCondition selectCondition, Map<String, Object> paramMap) {
        this.selectCondition = selectCondition;
        this.sql = sql;
        String[] sqlArr;
        this.paramMap = paramMap;
        if (sql.contains(" WHERE ")) {
            sqlArr = sql.split(WHERE + " " + OneEq);
            this.selecSql = sqlArr[0];
            this.conditionSql = sqlArr[1];
        } else if (sql.contains(" where ")) {
            sqlArr = sql.split(" where ");
            this.selecSql = sqlArr[0];
            this.conditionSql = sqlArr[1];
        } else {
            this.selecSql = sql;
        }
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public SelectCondition getSelectCondition() {
        return selectCondition;
    }

    public void setSelectCondition(SelectCondition selectCondition) {
        this.selectCondition = selectCondition;
    }

    public String getSelecSql() {
        return selecSql;
    }

    public void setSelecSql(String selecSql) {
        this.selecSql = selecSql;
    }

    public String getConditionSql() {
        return conditionSql;
    }

    public void setConditionSql(String conditionSql) {
        this.conditionSql = conditionSql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public static void JudesNull(Object object, String str) {
        if (object == null) {
            throw new ServiceException(str);
        }
    }

    public static ConditionBuilder creatCriteria() {
        return new ConditionBuilder();
    }

    public static ConditionBuilder creatCriteria(SelectCondition selectCondition) {
        return new ConditionBuilder(selectCondition,new HashMap<>());
    }

    public static ConditionBuilder creatCriteria(SelectCondition selectCondition, boolean flag) {
        return new ConditionBuilder(selectCondition, flag);
    }

    public static ConditionBuilder creatCriteria(DataMap dataMap) {
        JudesNull(dataMap.getTableName(), "tableName can not be null!");
        return new ConditionBuilder(dataMap.getSelectCondition(),new HashMap<>());
    }

    public static ConditionBuilder creatCriteria(DataMap dataMap, boolean flag) {
        JudesNull(dataMap.getTableName(), "tableName can not be null!");
        return new ConditionBuilder(dataMap.getSelectCondition(), flag);
    }

    /**
     * 是否开启防注入
     *
     * @param flag 默认开启
     * @return
     */
    public static ConditionBuilder creatCriteria(boolean flag) {
        return new ConditionBuilder();
    }

    public ConditionBuilder toCreatCriteria() {
        return new ConditionBuilder(this.sql,this.paramMap);
    }

    public ConditionBuilder toCreatCriteria(SelectCondition selectCondition) {
        return new ConditionBuilder(selectCondition, this.sql,this.paramMap);
    }

    public ConditionBuilder toCreatCriteria(DataMap dataMap) {
        return new ConditionBuilder(dataMap.getSelectCondition(), this.sql,this.paramMap);
    }

    public static class ConditionBuilder {

        private String sql = "";

        private String selecSql = "";

        private String conditionSql = "";

        private int lrn = 0;

        private int rrn = 0;
        private Map<String, Object> paramMap = new LinkedHashMap<>();

        // 是否开启防注入，默认开启
        private boolean flag = true;

        private SelectCondition selectCondition;

        public String getSelecSql() {
            return selecSql;
        }

        public void setSelecSql(String selecSql) {
            this.selecSql = selecSql;
        }

        public String getConditionSql() {
            return conditionSql;
        }

        public void setConditionSql(String conditionSql) {
            this.conditionSql = conditionSql;
        }

        public ConditionBuilder() {
        }

        public ConditionBuilder(boolean flag) {
            this.flag = flag;
        }

        public ConditionBuilder(SelectCondition selectCondition,Map<String,Object> paramMap) {
            this.selectCondition = selectCondition;
            this.selecSql = selectCondition.getSql();
            this.paramMap = paramMap;
        }

        public ConditionBuilder(SelectCondition selectCondition, boolean flag) {
            this.flag = flag;
            this.selectCondition = selectCondition;
            this.selecSql = selectCondition.getSql();
        }

        public ConditionBuilder(SelectCondition selectCondition, String sql,Map<String,Object> paramMap) {
            spliteSql(sql);
            this.selectCondition = selectCondition;
            this.selecSql = selectCondition.getSql();
            this.paramMap = paramMap;
        }

        private void spliteSql(String sql) {
            String[] sqlArr;
            if (sql.contains(" WHERE ")) {
                sqlArr = sql.split(WHERE + " " + OneEq);
                this.selecSql = sqlArr[0];
                this.conditionSql = sqlArr[1];
            } else if (sql.contains(" where ")) {
                sqlArr = sql.split(" where ");
                this.selecSql = sqlArr[0];
                this.conditionSql = sqlArr[1];
            } else {
                this.selecSql = sql;
            }
        }

        public ConditionBuilder(SelectCondition selectCondition, String sql, boolean flag) {
            this.flag = flag;
            spliteSql(sql);
            this.selectCondition = selectCondition;
            this.selecSql = selectCondition.getSql();
        }

        public ConditionBuilder(String sql,Map<String,Object> paramMap) {
            this.sql = sql;
            this.paramMap = paramMap;
            spliteSql(sql);
        }

        public ConditionBuilder leftCurves() {
            this.conditionSql += L_CURVES + " ";
            this.lrn++;
            return this;
        }

        public ConditionBuilder rightCurves() {
            this.conditionSql += " " + R_CURVES + " ";
            this.rrn++;
            return this;
        }

        public ConditionBuilder and() {
            this.conditionSql += " " + AND + " ";
            return this;
        }

        public ConditionBuilder or() {
            this.conditionSql += " " + OR + " ";
            return this;
        }

        public ConditionBuilder equal(String key, Object value) {
            if (value.toString().contains("\"")) {
                value = value.toString().replaceAll("\"", "");
            }
            String sqlKey = "item_" + IdUtil.fastUUID();
            this.conditionSql += " `" + key +"` "+ EQUAL + "#{" + sqlKey + "}";
            this.paramMap.put(sqlKey, value);
            return this;
        }

        public ConditionBuilder andEqual(String key, Object value) {
            this.and().equal(key, value);
            return this;
        }

        public ConditionBuilder orEqual(String key, Object value) {
            this.or().equal(key, value);
            return this;
        }

        public ConditionBuilder like(String key, Object value) {
            sqlCheckLike(value);
            this.conditionSql += " `" + key +"` "+ " " + LIKE + " '" + value + "'";
            return this;
        }

        public ConditionBuilder andLike(String key, Object value) {
            this.and().like(key, value);
            return this;
        }

        public ConditionBuilder orLike(String key, Object value) {
            this.or().like(key, value);
            return this;
        }


        public <E> ConditionBuilder in(String key, List<E> value) {
            StringBuilder inStr = new StringBuilder(L_CURVES);
            int i = 1;
            for (Object object : value) {
                String sqlKey = "item_" + IdUtil.fastUUID();
                this.paramMap.put(sqlKey, object);
                inStr.append("#{").append(sqlKey).append("}");
                if (i != value.size()) {
                    inStr.append(",");
                }
                i++;
            }
            inStr.append(R_CURVES);
            this.conditionSql += " `" + key +"` "+ " " + IN + " " + inStr.toString();
            return this;
        }

        public <E> ConditionBuilder andIn(String key, List<E> value) {
            this.and().in(key, value);
            return this;
        }

        public <E> ConditionBuilder orIn(String key, List<E> value) {
            this.or().in(key, value);
            return this;
        }

        public ConditionBuilder between(String key, Object start, Object end) {
            String sqlKey = "item_" + IdUtil.fastUUID();
            String sqlKey2 = "item_" + IdUtil.fastUUID();
            this.paramMap.put(sqlKey, start);
            this.paramMap.put(sqlKey2, end);
            this.conditionSql += " `" + key +"` "+ " " + BETWEEN + " #{" + sqlKey + "} " + AND + " #{" + sqlKey2 + "} ";
            return this;
        }

        public ConditionBuilder andBetween(String key, Object start, Object end) {
            this.and().between(key, start, end);
            return this;
        }

        public ConditionBuilder orBetween(String key, Object start, Object end) {
            this.or().between(key, start, end);
            return this;
        }

        public ConditionBuilder lt(String key, Object value) {
            String sqlKey = "item_" + IdUtil.fastUUID();
            this.paramMap.put(sqlKey, value);
            this.conditionSql += " `" + key +"` "+ " " + LT + " #{" + sqlKey + "} ";
            return this;
        }

        public ConditionBuilder andLt(String key, Object value) {
            this.and().lt(key, value);
            return this;
        }

        public ConditionBuilder orLt(String key, Object value) {
            this.or().lt(key, value);
            return this;
        }

        public ConditionBuilder not() {
            this.conditionSql += " " + NOT + " ";
            return this;
        }

        public ConditionBuilder notEqual(String key, Object value) {
            String sqlKey = "item_" + IdUtil.fastUUID();
            this.paramMap.put(sqlKey, value);
            this.conditionSql += " `" + key +"` "+ " " + NOT_EQUAL + " #{" + sqlKey + "} ";
            return this;
        }

        public ConditionBuilder andNotEqual(String key, Object value) {
            this.and().notEqual(key, value);
            return this;
        }

        public ConditionBuilder orNotEqual(String key, Object value) {
            this.or().notEqual(key, value);
            return this;
        }

        public ConditionBuilder gt(String key, Object value) {
            String sqlKey = "item_" + IdUtil.fastUUID();
            this.paramMap.put(sqlKey, value);
            this.conditionSql += " `" + key +"` "+ " " + GT + " #{" + sqlKey + "} ";
            return this;
        }

        public ConditionBuilder andGt(String key, Object value) {
            this.and().gt(key, value);
            return this;
        }

        public ConditionBuilder orGt(String key, Object value) {
            this.or().gt(key, value);
            return this;
        }

        public ConditionBuilder lte(String key, Object value) {
            String sqlKey = "item_" + IdUtil.fastUUID();
            this.paramMap.put(sqlKey, value);
            this.conditionSql += " `" + key +"` "+ " " + LTE + " #{" + value.toString() + "} ";
            return this;
        }

        public ConditionBuilder andLte(String key, Object value) {
            this.and().lte(key, value);
            return this;
        }

        public ConditionBuilder orLte(String key, Object value) {
            this.or().lte(key, value);
            return this;
        }

        public ConditionBuilder gte(String key, Object value) {
            String sqlKey = "item_" + IdUtil.fastUUID();
            this.paramMap.put(sqlKey, value);
            this.conditionSql += " `" + key +"` "+ " " + GTE + " #{" + value.toString() + "} ";
            return this;
        }

        public ConditionBuilder andGte(String key, Object value) {
            this.and().gte(key, value);
            return this;
        }

        public ConditionBuilder orGte(String key, Object value) {
            this.or().gte(key, value);
            return this;
        }

        public ConditionBuilder groupBy(String... str) {
            int i = 1;
            StringBuilder stringBuilder = new StringBuilder(" " + GROUP_BY + " ");
            for (String s : str) {
                stringBuilder.append(s);
                if (i != str.length) {
                    stringBuilder.append(",");
                }
                i++;
            }
            this.conditionSql += stringBuilder.toString();
            return this;
        }

        public void sqlCheck(Object obj) {
            if (org.springframework.util.StringUtils.isEmpty(obj)) {
                obj = "";
            }
            if (this.flag) {
                if (XssClass.sqlInj(obj.toString())) {
                    logger.error("非法字符：" + obj.toString());
                    throw new ServiceException("value中含有非法字符，有注入风险！");
                }
            }
        }

        public void sqlCheckLike(Object obj) {
            if (org.springframework.util.StringUtils.isEmpty(obj)) {
                obj = "";
            }
            if (this.flag) {
                if (XssClass.sqlInjLike(obj.toString())) {
                    logger.error("非法字符：" + obj.toString());
                    throw new ServiceException("value中含有非法字符，有注入风险！");
                }
            }
        }

        public Condition build() {
            if (this.rrn != this.lrn) {
                logger.error("sql语法错误，请检查小括号是否都闭合");
                logger.error("SQL语句：" + this.sql);
                throw new ServiceException("sql语法错误，请检查小括号是否都闭合");
            }
            this.sql = this.selecSql + " " + WHERE + " " + OneEq + " " + this.conditionSql;
            if (StringUtils.isEmpty(this.selecSql)) {
                this.sql = " " + WHERE + " " + OneEq + " " + this.conditionSql;
            }
            if (StringUtils.isEmpty(this.conditionSql)) {
                this.sql = this.selecSql;
            }
            return new Condition(this.sql, this.selectCondition,this.paramMap);
        }
    }

}
