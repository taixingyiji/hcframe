package com.hcframe.base.module.tableconfig.dao;

import com.hcframe.base.common.MyMapper;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OsSysTableMapper extends MyMapper<OsSysTable> {


    OsSysTable getTableAllInfo(@Param("typeName") String typeName);

}
