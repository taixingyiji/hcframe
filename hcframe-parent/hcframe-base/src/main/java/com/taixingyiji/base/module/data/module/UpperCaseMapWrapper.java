package com.taixingyiji.base.module.data.module;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className UpperCaseMapWrapper
 * @date 2025年03月28日 19:41
 * @description 描述
 */
public class UpperCaseMapWrapper extends MapWrapper {

    public UpperCaseMapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return name==null?"":name.toUpperCase() ;
    }
}
