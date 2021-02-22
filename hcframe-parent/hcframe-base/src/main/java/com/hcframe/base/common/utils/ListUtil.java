package com.hcframe.base.common.utils;


import com.hcframe.base.common.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ListUtil {

    public static Map<String,Object>  getValue(String value, List<Map<String,Object>> list,String key) {
        Optional<Map<String, Object>> objectMap = list
                .stream()
                .filter(r -> (Integer) r.get(key) == Integer.parseInt(value))
                .findFirst();
        if (objectMap.isPresent()) {
            return objectMap.get();
        } else {
            throw new ServiceException("key值不存在");
        }
    }
}
