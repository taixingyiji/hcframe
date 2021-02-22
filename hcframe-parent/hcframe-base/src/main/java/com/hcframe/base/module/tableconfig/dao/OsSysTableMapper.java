package com.hcframe.base.module.tableconfig.dao;

import com.hcframe.base.common.Mapper;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import org.apache.ibatis.annotations.Param;

public interface OsSysTableMapper extends Mapper<OsSysTable> {


    OsSysTable getTableAllInfo(@Param("typeName") String typeName);

}
