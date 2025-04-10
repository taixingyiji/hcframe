package com.taixingyiji.base.module.data.module;

import cn.hutool.core.date.DateUtil;

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
        Map<String, Object> convertedData = new HashMap<>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();

            String columnType = columnTypes.get(column.toLowerCase());
            if (value instanceof String) {
                if (columnType != null) {
                    switch (columnType) {
                        case "INTEGER":
                        case "INT":
                        case "integer":
                        case "int":
                            convertedData.put(column, Integer.parseInt(value.toString()));
                            break;
                        case "double":
                        case "DOUBLE":
                            convertedData.put(column, Double.parseDouble(value.toString()));
                            break;
                        case "float":
                        case "FLOAT":
                            convertedData.put(column, Float.parseFloat(value.toString()));
                            break;
                        case "bigint":
                        case "BIGINT":
                            convertedData.put(column, Long.parseLong(value.toString()));
                            break;
                        case "DATE":
                        case "DATETIME":
                        case "TIMESTAMP":
                        case "date":
                        case "datetime":
                        case "timestamp":
                            convertedData.put(column, DateUtil.parse(value.toString()));
                            break;
                        default:
                            if (columnType.contains("int") || columnType.contains("INT")) {
                                convertedData.put(column, Integer.parseInt(value.toString()));
                            } else if (columnType.contains("double") || columnType.contains("DOUBLE")) {
                                convertedData.put(column, Double.parseDouble(value.toString()));
                            } else if (columnType.contains("float") || columnType.contains("FLOAT")) {
                                convertedData.put(column, Float.parseFloat(value.toString()));
                            } else {
                                convertedData.put(column, value);
                            }
                            break;
                    }

                } else {
                    convertedData.put(column, value);
                }
            } else {
                convertedData.put(column, value);
            }
        }
        return convertedData;
    }

    public static Object convertDataTypes(String column, String value, Map<String, String> columnTypes) {
        Object result;
        String columnType = columnTypes.get(column.toLowerCase());
        if (columnType != null) {
            switch (columnType) {
                case "INTEGER":
                case "INT":
                case "integer":
                case "int":
                    result = Integer.parseInt(value);
                    break;
                case "bigint":
                case "BIGINT":
                    result = Long.parseLong(value);
                    break;
                case "DATE":
                case "DATETIME":
                case "TIMESTAMP":
                case "date":
                case "datetime":
                case "timestamp":
                    result = DateUtil.parse(value);
                    break;
                default:
                    result = value;
                    break;
            }
        } else {
            result = value;
        }
        return result;
    }
}
