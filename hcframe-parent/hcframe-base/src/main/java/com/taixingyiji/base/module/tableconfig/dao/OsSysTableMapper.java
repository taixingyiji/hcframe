package com.taixingyiji.base.module.tableconfig.dao;

import com.taixingyiji.base.common.MyMapper;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OsSysTableMapper extends MyMapper<OsSysTable> {


    OsSysTable getTableAllInfo(@Param("typeName") String typeName);

}
