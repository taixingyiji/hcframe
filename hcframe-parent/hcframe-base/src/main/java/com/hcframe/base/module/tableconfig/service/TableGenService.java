package com.hcframe.base.module.tableconfig.service;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.datasource.entity.DatasourceConfig;
import com.hcframe.base.module.tableconfig.entity.FieldInfo;
import com.hcframe.base.module.tableconfig.entity.TableInfo;

public interface TableGenService {
    ResultVO getTableList(WebPageInfo webPageInfo, TableInfo tableInfo, DatasourceConfig datasourceConfig);

    ResultVO genTable(DatasourceConfig datasourceConfig);

    ResultVO genAllSequence();

    ResultVO genAll(DatasourceConfig datasourceConfig);

    ResultVO getFieldList(WebPageInfo webPageInfo, FieldInfo fieldInfo, DatasourceConfig datasourceConfig, String tableName);

    ResultVO genField(DatasourceConfig datasourceConfig, String tableAlias);
}
