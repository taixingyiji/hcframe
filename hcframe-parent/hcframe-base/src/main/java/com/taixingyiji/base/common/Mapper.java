package com.taixingyiji.base.common;

import tk.mybatis.mapper.common.*;

/***
 * @description 调用tkmapper进行接口封装
 * @param <T>
 */
public interface Mapper<T> extends
        BaseMapper<T>,
        ExampleMapper<T>,
        RowBoundsMapper<T>,
        Marker, ConditionMapper<T>, tk.mybatis.mapper.common.Mapper<T> {

}
