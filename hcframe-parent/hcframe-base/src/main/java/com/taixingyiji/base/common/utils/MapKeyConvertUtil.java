package com.taixingyiji.base.common.utils;
import java.util.*;

public class MapKeyConvertUtil {
    public static Object camelToUpperUnderlineObject(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                Object value = entry.getValue();

                result.put(camelToUpperUnderline(key), camelToUpperUnderlineObject(value));
            }

            return result;
        }

        if (obj instanceof List<?> list) {
            List<Object> result = new ArrayList<>();
            for (Object item : list) {
                result.add(camelToUpperUnderlineObject(item));
            }
            return result;
        }

        if (obj instanceof Set<?> set) {
            Set<Object> result = new LinkedHashSet<>();
            for (Object item : set) {
                result.add(camelToUpperUnderlineObject(item));
            }
            return result;
        }

        return obj;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> camelToUpperUnderlineMap(Map<String, Object> map) {
        return (Map<String, Object>) camelToUpperUnderlineObject(map);
    }

    public static String camelToUpperUnderline(String str) {
        if (str == null || str.isBlank()) {
            return str;
        }

        return str
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .toUpperCase();
    }
}
