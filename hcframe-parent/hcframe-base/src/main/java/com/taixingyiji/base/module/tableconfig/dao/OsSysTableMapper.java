package com.taixingyiji.base.module.tableconfig.dao;

import com.taixingyiji.base.common.Mapper;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import org.apache.ibatis.annotations.Param;

public interface OsSysTableMapper extends Mapper<OsSysTable> {


    OsSysTable getTableAllInfo(@Param("typeName") String typeName);

}
