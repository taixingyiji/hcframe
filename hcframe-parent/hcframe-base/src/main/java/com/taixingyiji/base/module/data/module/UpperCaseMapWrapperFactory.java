package com.taixingyiji.base.module.data.module;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className UpperCaseMapWrapperFactory
 * @date 2025年03月28日 19:40
 * @description 描述
 */
public class UpperCaseMapWrapperFactory implements ObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object object) {
        return object instanceof Map;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new UpperCaseMapWrapper(metaObject, (Map) object);
    }

}
