package com.hcframe.user.module.userinfo.service.impl;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.module.userinfo.service.OrgService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

/**
 * @author lhc
 */
@Service
public class OrgServiceImpl implements OrgService {

    private static final String ORG_ID = "ORG_ID";
    private static final String OS_SYS_ORG = "OS_SYS_ORG";
    private static final String OS_SYS_POSITION = "OS_SYS_POSITION";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(OS_SYS_ORG).tablePk(ORG_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    public OrgServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                          TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    public ResultVO<Object> addOrg(Map<String, Object> org) {
        baseMapper.save(OS_SYS_ORG,ORG_ID,org);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Integer> updateOrg(Map<String, Object> org, Integer version) {
        return tableService.updateWithDate(TABLE_INFO,org,version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> deleteOrg(String ids) {
        String[] idArr = ids.split(",");
        Condition condition = Condition
                .creatCriteria()
                .andIn(ORG_ID,Arrays.asList(idArr))
                .build();
        baseMapper.deleteByCondition(OS_SYS_POSITION,condition);
        tableService.delete(TABLE_INFO, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getOrgList(String data, WebPageInfo webPageInfo) {
        PageInfo<Map<String,Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
        return ResultVO.getSuccess(pageInfo);
    }

}
