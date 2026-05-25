package com.taixingyiji.base.module.data.module;

import cn.hutool.core.date.DateUtil;
import com.taixingyiji.base.common.ServiceException;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author lhc
 * @version 1.0
 * @className DataTypeConverter
 * @date 2025年04月09日 15:17
 * @description 描述
 */
public class DataTypeConverter {
    // 缓存字段类型信息
    // 自动转换数据类型
    public static Map<String, Object> convertDataTypes(Map<String, Object> data, Map<String, String> columnTypes) {
        Map<String, Object> convertedData = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();

            convertedData.put(column, convertDataTypes(column, value, columnTypes));
        }
        return convertedData;
    }

    public static Object convertDataTypes(String column, String value, Map<String, String> columnTypes) {
        return convertDataTypes(column, (Object) value, columnTypes);
    }

    public static Object convertDataTypes(String column, Object value, Map<String, String> columnTypes) {
        if (!(value instanceof String) || columnTypes == null) {
            return value;
        }
        String columnType = getColumnType(column, columnTypes);
        if (columnType == null) {
            return value;
        }
        String strValue = value.toString();
        if (strValue.trim().isEmpty()) {
            return value;
        }
        try {
            return convertStringValue(strValue, columnType);
        } catch (RuntimeException e) {
            throw new ServiceException("字段[" + column + "]类型[" + columnType + "]值[" + strValue + "]转换失败");
        }
    }

    private static String getColumnType(String column, Map<String, String> columnTypes) {
        String columnType = columnTypes.get(column);
        if (columnType == null) {
            columnType = columnTypes.get(column.toLowerCase());
        }
        if (columnType == null) {
            columnType = columnTypes.get(column.toUpperCase());
        }
        return columnType;
    }

    private static Object convertStringValue(String value, String columnType) {
        String type = columnType.toLowerCase(Locale.ROOT);
        if (isLongType(type)) {
            return Long.parseLong(value);
        }
        if (isIntegerType(type)) {
            return Integer.parseInt(value);
        }
        if (isDecimalType(type)) {
            return new BigDecimal(value);
        }
        if (isDoubleType(type)) {
            return Double.parseDouble(value);
        }
        if (isFloatType(type)) {
            return Float.parseFloat(value);
        }
        if (isBooleanType(type)) {
            return parseBoolean(value);
        }
        if (isDateType(type)) {
            return DateUtil.parse(value);
        }
        return value;
    }

    private static boolean isIntegerType(String type) {
        return type.equals("int")
                || type.equals("int2")
                || type.equals("int4")
                || type.equals("integer")
                || type.equals("smallint")
                || type.contains("serial");
    }

    private static boolean isLongType(String type) {
        return type.equals("int8")
                || type.equals("bigint")
                || type.equals("bigserial")
                || type.contains("bigint");
    }

    private static boolean isDecimalType(String type) {
        return type.equals("numeric")
                || type.equals("decimal")
                || type.startsWith("numeric(")
                || type.startsWith("decimal(")
                || type.equals("number");
    }

    private static boolean isDoubleType(String type) {
        return type.equals("double")
                || type.equals("float8")
                || type.equals("double precision");
    }

    private static boolean isFloatType(String type) {
        return type.equals("float")
                || type.equals("float4")
                || type.equals("real");
    }

    private static boolean isBooleanType(String type) {
        return type.equals("bool")
                || type.equals("boolean");
    }

    private static boolean isDateType(String type) {
        return type.equals("date")
                || type.equals("datetime")
                || type.equals("timestamp")
                || type.equals("timestamptz")
                || type.equals("timestampz")
                || type.equals("timestamp with time zone")
                || type.equals("timestamp without time zone")
                || type.equals("time")
                || type.equals("time with time zone")
                || type.equals("time without time zone");
    }

    private static Boolean parseBoolean(String value) {
        String str = value.trim().toLowerCase(Locale.ROOT);
        switch (str) {
            case "1":
            case "t":
            case "true":
            case "y":
            case "yes":
                return true;
            case "0":
            case "f":
            case "false":
            case "n":
            case "no":
                return false;
            default:
                throw new IllegalArgumentException("Unsupported boolean value: " + value);
        }
    }
}
