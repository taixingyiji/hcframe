package com.hcframe.base.module.tableconfig.dao;


import com.hcframe.base.common.Mapper;
import com.hcframe.base.module.tableconfig.entity.OsSysField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (OsSysField)表数据库访问层
 *
 * @author lhc
 * @since 2020-03-17 17:26:53
 */
public interface OsSysFieldDao extends Mapper<OsSysField> {

    List<String> getTableAlise(@Param("ids") String ids);

}
