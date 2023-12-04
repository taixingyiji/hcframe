package com.taixingyiji.base.module.datasource.dao;

import com.taixingyiji.base.common.Mapper;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;

/**
 * (DatasourceConfig)表数据库访问层
 *
 * @author lhc
 * @since 2020-09-23 09:28:03
 */
public interface DatasourceConfigDao extends Mapper<DatasourceConfig> {


    int  getCount();
}
