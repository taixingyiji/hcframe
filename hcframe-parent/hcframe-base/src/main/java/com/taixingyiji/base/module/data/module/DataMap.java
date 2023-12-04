package com.taixingyiji.base.module.data.module;

import com.taixingyiji.base.common.utils.ObjectUtil;
import com.taixingyiji.base.module.shiro.FtToken;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @date 2020-12-23
 * @decription 数据库dataMap泛型
 */
public class DataMap<T> implements Serializable {
    private static final long serialVersionUID = -931903976665708139L;
    public static final String VERSION = "VERSION";
    private Map<String, Object> data = new HashMap();
    private String ids;
    private List<String> idList;
    private String tableName;
    private String pkName;
    private Object pkValue;
    private String fields = "*";
    private List<String> fieldList;
    private String subData;
    private String selectSql;
    private SelectCondition selectCondition;
    private T obj;

    public T getObj() {
        return obj;
    }

    public void set(String key, Object value) {
        ObjectUtil.setField(this.obj, key, value);
    }

    public void setObj(T obj) {
        this.obj = obj;
        ObjectUtil.objToSqlMap(obj, this);
    }

    public DataMap(OsSysTable osSysTable) {
        this.pkName = osSysTable.getTablePk();
        this.tableName = osSysTable.getTableName();
    }

    public List<String> getIdList() {
        if (this.idList == null) {
            String[] strArr = this.ids.split(",");
            this.idList = Arrays.asList(strArr);
        }
        return this.idList;
    }

    public List<String> getFieldList() {
        if (this.fieldList == null) {
            String[] strArr = this.fields.split(",");
            this.fieldList = Arrays.asList(strArr);
        }
        return this.fieldList;
    }

    public String getSelectSql() {
        SelectCondition selectCondition = SelectCondition.builder()
                .tableName(this.tableName)
                .field(this.getFieldList())
                .build();
        this.selectSql = selectCondition.getSql();
        return this.selectSql;
    }

    public SelectCondition getSelectCondition() {
        return SelectCondition.builder().tableName(this.tableName).field(this.getFieldList()).build();
    }

    public static <T> DataMapBuilder<T> builder() {
        return new DataMapBuilder<T>();
    }

    public DataMapBuilder<T> toBuilder() {
        return (new DataMapBuilder<T>()).data(this.data).ids(this.ids).idList(this.idList).tableName(this.tableName).pkName(this.pkName).pkValue(this.pkValue).fields(this.fields).fieldList(this.fieldList).subData(this.subData).selectSql(this.selectSql).selectCondition(this.selectCondition);
    }

    public Object get(String key) {
        return this.data.get(key);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DataMap)) {
            return false;
        } else {
            DataMap other = (DataMap) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label143:
                {
                    Object this$data = this.getData();
                    Object other$data = other.getData();
                    if (this$data == null) {
                        if (other$data == null) {
                            break label143;
                        }
                    } else if (this$data.equals(other$data)) {
                        break label143;
                    }

                    return false;
                }

                Object this$ids = this.getIds();
                Object other$ids = other.getIds();
                if (this$ids == null) {
                    if (other$ids != null) {
                        return false;
                    }
                } else if (!this$ids.equals(other$ids)) {
                    return false;
                }

                Object this$idList = this.getIdList();
                Object other$idList = other.getIdList();
                if (this$idList == null) {
                    if (other$idList != null) {
                        return false;
                    }
                } else if (!this$idList.equals(other$idList)) {
                    return false;
                }

                label122:
                {
                    Object this$tableName = this.getTableName();
                    Object other$tableName = other.getTableName();
                    if (this$tableName == null) {
                        if (other$tableName == null) {
                            break label122;
                        }
                    } else if (this$tableName.equals(other$tableName)) {
                        break label122;
                    }

                    return false;
                }

                label115:
                {
                    Object this$pkName = this.getPkName();
                    Object other$pkName = other.getPkName();
                    if (this$pkName == null) {
                        if (other$pkName == null) {
                            break label115;
                        }
                    } else if (this$pkName.equals(other$pkName)) {
                        break label115;
                    }

                    return false;
                }

                Object this$pkValue = this.getPkValue();
                Object other$pkValue = other.getPkValue();
                if (this$pkValue == null) {
                    if (other$pkValue != null) {
                        return false;
                    }
                } else if (!this$pkValue.equals(other$pkValue)) {
                    return false;
                }

                Object this$fields = this.getFields();
                Object other$fields = other.getFields();
                if (this$fields == null) {
                    if (other$fields != null) {
                        return false;
                    }
                } else if (!this$fields.equals(other$fields)) {
                    return false;
                }

                label94:
                {
                    Object this$fieldList = this.getFieldList();
                    Object other$fieldList = other.getFieldList();
                    if (this$fieldList == null) {
                        if (other$fieldList == null) {
                            break label94;
                        }
                    } else if (this$fieldList.equals(other$fieldList)) {
                        break label94;
                    }

                    return false;
                }

                label87:
                {
                    Object this$subData = this.getSubData();
                    Object other$subData = other.getSubData();
                    if (this$subData == null) {
                        if (other$subData == null) {
                            break label87;
                        }
                    } else if (this$subData.equals(other$subData)) {
                        break label87;
                    }

                    return false;
                }

                Object this$selectSql = this.getSelectSql();
                Object other$selectSql = other.getSelectSql();
                if (this$selectSql == null) {
                    if (other$selectSql != null) {
                        return false;
                    }
                } else if (!this$selectSql.equals(other$selectSql)) {
                    return false;
                }

                Object this$selectCondition = this.getSelectCondition();
                Object other$selectCondition = other.getSelectCondition();
                if (this$selectCondition == null) {
                    if (other$selectCondition != null) {
                        return false;
                    }
                } else if (!this$selectCondition.equals(other$selectCondition)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DataMap;
    }


    public DataMap() {
    }

    public DataMap(final Map<String, Object> data, final String ids, final List<String> idList, final String tableName, final String pkName, final Object pkValue, final String fields, final List<String> fieldList, final String subData, final String selectSql, final SelectCondition selectCondition, final T obj) {
        this.data = data;
        this.ids = ids;
        this.idList = idList;
        this.tableName = tableName;
        this.pkName = pkName;
        this.pkValue = pkValue;
        this.fields = fields;
        this.fieldList = fieldList;
        this.subData = subData;
        this.selectSql = selectSql;
        this.selectCondition = selectCondition;
        this.obj = obj;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public String getIds() {
        return this.ids;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getPkName() {
        return this.pkName;
    }

    public Object getPkValue() {
        return this.pkValue;
    }

    public String getFields() {
        return this.fields;
    }

    public String getSubData() {
        return this.subData;
    }

    public void setData(final Map<String, Object> data) {
        this.data = data;
    }

    public void setIds(final String ids) {
        this.ids = ids;
    }

    public void setIdList(final List<String> idList) {
        this.idList = idList;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public void setPkName(final String pkName) {
        this.pkName = pkName;
    }

    public void setPkValue(final Object pkValue) {
        this.pkValue = pkValue;
    }

    public void setFields(final String fields) {
        this.fields = fields;
    }

    public void setFieldList(final List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public void setSubData(final String subData) {
        this.subData = subData;
    }

    public void setSelectSql(final String selectSql) {
        this.selectSql = selectSql;
    }

    public void setSelectCondition(final SelectCondition selectCondition) {
        this.selectCondition = selectCondition;
    }


    public static class DataMapBuilder<T> {
        private Map<String, Object> data = new HashMap<>();
        private String ids;
        private List<String> idList;
        private String tableName;
        private String pkName;
        private Object pkValue;
        private String fields = "*";
        private List<String> fieldList;
        private String subData;
        private String selectSql;
        private SelectCondition selectCondition;
        private T obj;

        DataMapBuilder() {
        }

        public DataMapBuilder<T> obj(T object) {
            this.obj = object;
            ObjectUtil.objToSqlMap(object, this);
            return this;
        }

        public <E> DataMapBuilder<T> set(String key, Object value) {
            ObjectUtil.setField(this.obj, key, value);
            return this;
        }

        public DataMapBuilder<T> add(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public Object get(String key) {
            return this.data.get(key);
        }

        public DataMapBuilder<T> remove(String key) {
            this.data.remove(key);
            return this;
        }

        public DataMapBuilder<T> remove(String key, Object value) {
            this.data.remove(key, value);
            return this;
        }

        public DataMapBuilder<T> data(final Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public DataMapBuilder<T> ids(final String ids) {
            this.ids = ids;
            return this;
        }

        public DataMapBuilder<T> idList(final List<String> idList) {
            this.idList = idList;
            return this;
        }

        public DataMapBuilder<T> tableName(final String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DataMapBuilder<T> pkName(final String pkName) {
            this.pkName = pkName;
            return this;
        }

        public DataMapBuilder<T> pkValue(final Object pkValue) {
            this.pkValue = pkValue;
            return this;
        }

        public DataMapBuilder<T> fields(final String fields) {
            this.fields = fields;
            return this;
        }

        public DataMapBuilder<T> sysOsTable(OsSysTable table) {
            this.pkName = table.getTablePk();
            this.tableName = table.getTableName();
            return this;
        }

        public DataMapBuilder<T> fieldList(final List<String> fieldList) {
            this.fieldList = fieldList;
            return this;
        }

        public DataMapBuilder<T> subData(final String subData) {
            this.subData = subData;
            return this;
        }

        public DataMapBuilder<T> selectSql(final String selectSql) {
            this.selectSql = selectSql;
            return this;
        }

        public DataMapBuilder<T> selectCondition(final SelectCondition selectCondition) {
            this.selectCondition = selectCondition;
            return this;
        }

        public DataMap<T> build() {
            return new DataMap<>(this.data, this.ids, this.idList, this.tableName, this.pkName, this.pkValue, this.fields, this.fieldList, this.subData, this.selectSql, this.selectCondition, this.obj);
        }

    }

    public static void main(String[] args) {
        Long l = 1L;
        FtToken ftToken = new FtToken();
        DataMap<FtToken> dataMap = DataMap.<FtToken>builder().obj(ftToken).build();
        System.out.println(dataMap.getSelectSql());
    }
}
