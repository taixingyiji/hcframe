package com.hcframe.base.module.tableconfig.service;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.tableconfig.entity.OsSysField;
import com.hcframe.base.module.tableconfig.entity.OsSysSelect;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;

public interface TableConfigService {

    ResultVO getTableInfo(OsSysTable osSysTable, WebPageInfo webPageInfo);

    ResultVO saveTableInfo(OsSysTable osSysTable);

    ResultVO deleteTable(String ids);

    ResultVO updateTable(OsSysTable osSysTable);

    ResultVO getFieldList(OsSysField osSysField, WebPageInfo webPageInfo);

    ResultVO saveFieldInfo(OsSysField osSysField);

    ResultVO updateFieldInfo(OsSysField osSysField);

    ResultVO deleteField(String ids);

    ResultVO getSelectList(OsSysSelect osSysSelect);

    ResultVO saveSelectInfo(OsSysSelect osSysSelect);

    ResultVO updateSelectInfo(OsSysSelect osSysSelect);

    ResultVO deleteSelectInfo(String ids);

    ResultVO getTableSelect();

    ResultVO fieldSort(Integer tableId);

    ResultVO upMove(Integer id);

    ResultVO downMove(Integer id);
}
