package com.taixingyiji.base.module.data.module;

import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lhc
 * @date 2020-12-23
 * @decription select语句拼装工具类
 */
@Component
public class SelectCondition implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(SelectCondition.class);
    private static final long serialVersionUID = 4068737478156616795L;

    private static String SELECT = "SELECT";
    private static String FROM = "FROM";

    private String sql = "";

    private String tableName = "";

    public SelectCondition() {
    }

    public SelectCondition(String sql) {
        this.sql = sql;
    }

    public SelectCondition(String sql, String tableName) {
        this.sql = sql;
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public static SelectBuilder builder() {
        return new SelectBuilder();
    }

    public SelectBuilder toBuilder() {
        return new SelectBuilder(this.sql);
    }

    public static SelectJoinBuilder joinBuilder(String tableName) {
        return new SelectJoinBuilder(tableName);
    }

    public SelectJoinBuilder toJoinBuilder() {
        return new SelectJoinBuilder(this.sql, this.tableName);
    }

    public static SelectSqlJoinBuilder sqlJoinBuilder(String tableName) {
        return new SelectSqlJoinBuilder(tableName);
    }

    public SelectSqlJoinBuilder toSqlJoinBuilder() {
        return new SelectSqlJoinBuilder(this.sql, this.tableName);
    }


    private static String getFieldStr(List<String> fieldList) {
        StringBuilder stringBuilder = new StringBuilder();
        if (fieldList != null && fieldList.size() > 0) {
            int i = 1;
            for (String field : fieldList) {
                stringBuilder.append(field);
                if (i != fieldList.size()) {
                    stringBuilder.append(",");
                }
                i++;
            }
            return stringBuilder.toString();
        } else {
            return " * ";
        }
    }

    public static class SelectBuilder {
        private String sql = "";

        private List<String> fieldList = new LinkedList<>();

        private String tableName;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public SelectBuilder() {
        }

        public SelectBuilder(String sql) {
            this.sql = sql;
        }

        public SelectBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public SelectBuilder field(String field) {
            this.fieldList.add(field);
            return this;
        }

        public SelectBuilder field(List<String> field) {
            this.fieldList.addAll(field);
            return this;
        }

        public SelectCondition build() {
            if (StringUtils.isEmpty(this.tableName)) {
                throw new ServiceException("表名不能为空");
            }
            this.sql = SELECT + " ";
            this.sql += getFieldStr(this.fieldList);
            this.sql += " " + FROM + " " + this.tableName;
            return new SelectCondition(this.sql);
        }
    }

    public static class SelectJoinBuilder {
        private String sql = "";

        private String tableName;

        private List<String> fieldList = new LinkedList<>();

        private List<JoinCondition> joinConditions = new LinkedList<>();

        private List<JoinCondition> leftConditions = new LinkedList<>();

        private List<JoinCondition> rightConditions = new LinkedList<>();

        public SelectJoinBuilder(String tableName) {
            this.tableName = tableName;
        }

        public SelectJoinBuilder(String sql, String tableName) {
            this.tableName = tableName;
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public SelectJoinBuilder field(String field) {
            this.fieldList.add(field);
            return this;
        }

        public SelectJoinBuilder field(List<String> field) {
            this.fieldList.addAll(field);
            return this;
        }

        public SelectJoinBuilder join(JoinCondition joinCondition) {
            this.joinConditions.add(joinCondition);
            return this;
        }

        public SelectJoinBuilder join(List<JoinCondition> joinConditions) {
            this.joinConditions.addAll(joinConditions);
            return this;
        }
        public SelectJoinBuilder rightJoin(JoinCondition joinCondition) {
            this.rightConditions.add(joinCondition);
            return this;
        }

        public SelectJoinBuilder rightJoin(List<JoinCondition> joinConditions) {
            this.rightConditions.addAll(joinConditions);
            return this;
        }

        public SelectJoinBuilder leftJoin(JoinCondition joinCondition) {
            this.leftConditions.add(joinCondition);
            return this;
        }

        public SelectJoinBuilder leftJoin(List<JoinCondition> joinConditions) {
            this.leftConditions.addAll(joinConditions);
            return this;
        }

        public SelectCondition build() {
            StringBuilder stringBuilder = new StringBuilder(" ");
            if (StringUtils.isEmpty(this.sql)) {
                this.sql = SELECT + " ";
                this.sql += getFieldStr(this.fieldList);
                stringBuilder.append(" FROM ");
                stringBuilder.append(this.tableName).append(" ");
            }
            for (JoinCondition condition : this.joinConditions) {
                stringBuilder
                        .append(" JOIN ")
                        .append(condition.getName())
                        .append(" ON ")
                        .append(condition.getName())
                        .append(".")
                        .append(condition.getField())
                        .append(" = ")
                        .append(condition.getFkTable())
                        .append(".")
                        .append(condition.getJoinField())
                        .append(" ");
            }

            for (JoinCondition condition : this.leftConditions) {
                stringBuilder
                        .append(" LEFT JOIN ")
                        .append(condition.getName())
                        .append(" ON ")
                        .append(condition.getName())
                        .append(".")
                        .append(condition.getField())
                        .append(" = ")
                        .append(condition.getFkTable())
                        .append(".")
                        .append(condition.getJoinField())
                        .append(" ");
            }

            for (JoinCondition condition : this.rightConditions) {
                stringBuilder
                        .append(" RIGHT JOIN ")
                        .append(condition.getName())
                        .append(" ON ")
                        .append(condition.getName())
                        .append(".")
                        .append(condition.getField())
                        .append(" = ")
                        .append(condition.getFkTable())
                        .append(".")
                        .append(condition.getJoinField())
                        .append(" ");
            }
            this.sql += stringBuilder.toString();
            return new SelectCondition(this.sql);
        }
    }

    public static class SelectSqlJoinBuilder {
        private String sql = "";

        private String joinSql = "";

        private String tableName;

        private String tempTable;

        private List<String> fieldList = new LinkedList<>();

        public SelectSqlJoinBuilder(String tableName) {
            this.tableName = tableName;
        }

        public SelectSqlJoinBuilder(String sql, String tableName) {
            this.sql = sql;
            this.tableName = tableName;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public SelectSqlJoinBuilder field(String field) {
            this.fieldList.add(field);
            return this;
        }

        public SelectSqlJoinBuilder field(List<String> field) {
            this.fieldList.addAll(field);
            return this;
        }

        public SelectSqlJoinBuilder join(String tableName) {
            this.tempTable = tableName;
            this.joinSql += " JOIN " + tableName + " ";
            return this;
        }

        public SelectSqlJoinBuilder leftJoin(String tableName) {
            this.tempTable = tableName;
            this.joinSql += " LEFT JOIN " + tableName + " ";
            return this;
        }

        public SelectSqlJoinBuilder rightJoin(String tableName) {
            this.tempTable = tableName;
            this.joinSql += " RIGHT JOIN " + tableName + " ";
            return this;
        }

        public SelectSqlJoinBuilder on(String field, String joinTable, String joinField) {
            this.joinSql += " ON " + this.tempTable + "." + field + "=" + joinTable + "." + joinField + " ";
            return this;
        }

        public SelectCondition build() {
            if (StringUtils.isEmpty(this.sql)) {
                this.sql = SELECT + " ";
                this.sql += getFieldStr(this.fieldList);
                StringBuilder stringBuilder = new StringBuilder(" FROM ");
                stringBuilder.append(this.tableName).append(" ");
                this.sql += stringBuilder.toString();
            }
            this.sql += this.joinSql;
            return new SelectCondition(this.sql, this.tableName);
        }
    }
}
