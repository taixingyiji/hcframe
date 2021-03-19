package com.hcframe.base.common.utils;


import com.hcframe.base.common.ServiceException;
import com.hcframe.base.module.data.annotation.DataIgnore;
import com.hcframe.base.module.data.exception.BaseMapperException;
import com.hcframe.base.module.data.module.DataMap;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtil {

    private static final String SERIAL_VERSION_UID = "serialVersionUID";
    private static final String STRING = "class java.lang.String";
    private static final String INTEGER = "class java.lang.Integer";
    private static final String LONG = "class java.lang.LONG";

    public static Map<String, Object> objToMap(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> map = new HashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                if (!org.springframework.util.StringUtils.isEmpty(field.get(obj)) && !fieldName.equals(SERIAL_VERSION_UID)) {
                    Object value = field.get(obj);
                    map.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static <T> void objToSqlMap(Object obj, DataMap<T> dataMap) {
        Class<?> clazz = obj.getClass();
        Map<String, Object> map = new HashMap<String, Object>(clazz.getDeclaredFields().length);
        List<String> fieldList = new ArrayList<>();
        if (clazz.getAnnotation(Table.class) != null) {
            Table table = clazz.getAnnotation(Table.class);
            dataMap = dataMap.toBuilder().tableName(table.name()).build();
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (field.getAnnotation(Id.class) != null) {
                dataMap = dataMap.toBuilder().pkName(StringUtils.toUnderScoreUpperCase(fieldName)).build();
                try {
                    if (!org.springframework.util.StringUtils.isEmpty(field.get(obj))) {
                        dataMap.setPkValue(field.get(obj));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (!fieldName.equals(SERIAL_VERSION_UID)) {
                    if (!org.springframework.util.StringUtils.isEmpty(field.get(obj))) {
                        Object value = field.get(obj);
                        map.put(StringUtils.toUnderScoreUpperCase(fieldName), value);
                    }
                    if (field.getAnnotation(DataIgnore.class) == null) {
                        fieldList.add(StringUtils.toUnderScoreUpperCase(fieldName));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        dataMap.toBuilder().data(map).fieldList(fieldList).build();
    }

    public static <T> void objToSqlMap(Object obj, DataMap.DataMapBuilder<T> dataMap) {
        Class<?> clazz = obj.getClass();
        Map<String, Object> map = new HashMap<String, Object>(clazz.getDeclaredFields().length);
        List<String> fieldList = new ArrayList<>();
        if (clazz.getAnnotation(Table.class) != null) {
            Table table = clazz.getAnnotation(Table.class);
            if (StringUtils.isEmpty(table.name())) {
                throw new BaseMapperException("请检查实体类是否添加注解@Table,并且name中是否正确设置了表名");
            }
            dataMap = dataMap.tableName(table.name());
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (field.getAnnotation(Id.class) != null) {
                dataMap = dataMap.pkName(StringUtils.toUnderScoreUpperCase(fieldName));
                try {
                    if (!org.springframework.util.StringUtils.isEmpty(field.get(obj))) {
                        dataMap.pkValue(field.get(obj));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (!fieldName.equals(SERIAL_VERSION_UID)) {
                    if (!org.springframework.util.StringUtils.isEmpty(field.get(obj))) {
                        Object value = field.get(obj);
                        map.put(StringUtils.toUnderScoreUpperCase(fieldName), value);
                    }
                    if (field.getAnnotation(DataIgnore.class) == null) {
                        fieldList.add(StringUtils.toUnderScoreUpperCase(fieldName));
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        dataMap.data(map).fieldList(fieldList);
    }

    public static void setField(Object obj, String key, Object value) {
        Class<?> clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField(key);
            field.setAccessible(true);
            String str = field.getGenericType().toString();
            if (STRING.equals(str)) {
                value = value.toString();
            }
            if (INTEGER.equals(str)) {
                value = Integer.valueOf(value.toString());
            }
            if (LONG.equals(str)) {
                value = Long.valueOf(value.toString());
            }
            try {
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                throw new ServiceException("字段类型不匹配", e);
            }
        } catch (NoSuchFieldException e) {
            throw new ServiceException("无法找到相应的字段", e);
        }
    }

}
