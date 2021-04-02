package com.hcframe.user.module.auth.service.impl;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.module.auth.service.RoleGroupServie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleGroupService
 * @date 2021年04月02日 10:10 上午
 * @description 描述
 */
@Service
public class RoleGroupServiceImpl implements RoleGroupServie {

    private static final String PK_ID = "GROUP_ID";
    private static final String TABLE_NAME = "OS_SYS_ROLE_GROUP";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(TABLE_NAME).tablePk(PK_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    public RoleGroupServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                           TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }


    @Override
    public ResultVO<Map<String,Object>> add(Map<String, Object> roleGroup) {
        return tableService.saveWithDate(TABLE_INFO,roleGroup);
    }

    @Override
    public ResultVO<Integer> update(Map<String, Object> roleGroup, Integer version) {
        return tableService.updateWithDate(TABLE_INFO, roleGroup, version);
    }

    @Override
    public ResultVO<Integer> delete(String ids) {
        // TODO 补全删除的关联信息
        return tableService.delete(TABLE_INFO, ids);
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getList(String data, WebPageInfo webPageInfo) {
        return ResultVO.getSuccess(tableService.searchSingleTables(data, TABLE_INFO, webPageInfo));
    }
}
