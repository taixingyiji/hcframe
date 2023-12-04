package com.taixingyiji.base.module.tableconfig.service;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.taixingyiji.base.module.tableconfig.entity.FieldInfo;
import com.taixingyiji.base.module.tableconfig.entity.TableInfo;

public interface TableGenService {
    ResultVO getTableList(WebPageInfo webPageInfo, TableInfo tableInfo, DatasourceConfig datasourceConfig);

    ResultVO genTable(DatasourceConfig datasourceConfig);

    ResultVO genAllSequence();

    ResultVO genAll(DatasourceConfig datasourceConfig);

    ResultVO getFieldList(WebPageInfo webPageInfo, FieldInfo fieldInfo, DatasourceConfig datasourceConfig, String tableName);

    ResultVO genField(DatasourceConfig datasourceConfig, String tableAlias);
}
