package com.taixingyiji.base.module.tableconfig.dao;


import com.taixingyiji.base.common.MyMapper;
import com.taixingyiji.base.module.tableconfig.entity.OsSysField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (OsSysField)表数据库访问层
 *
 * @author lhc
 * @since 2020-03-17 17:26:53
 */
@Mapper
public interface OsSysFieldDao extends MyMapper<OsSysField> {
    List<String> getTableAlise(@Param("ids") String ids);

}
