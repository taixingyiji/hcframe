package com.taixingyiji.user.module.userinfo.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.user.module.userinfo.service.OsService;

@Service
public class OsServiceImpl implements OsService {

    private static final String OS_ID = "OS_ID";
    private static final String OS_SYS_OS = "OS_SYS_OS";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(OS_SYS_OS).tablePk(OS_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    public OsServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                         TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    public ResultVO<Object> checkExistOs(String oscode) {
        Condition condition = Condition.creatCriteria().andEqual("OS_CODE", oscode).build();
        Long i = baseMapper.count(OS_SYS_OS, condition);
        if (i > 0L) {
            return ResultVO.getFailed("系统编码不能重复");
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Object> addOs(Map<String, Object> os) {
        return ResultVO.getSuccess(tableService.saveWithDate(TABLE_INFO, os));
    }

    @Override
    public ResultVO<Map<String,Object>> updateOs(Map<String, Object> os, Integer version) {
        return tableService.updateWithDate(TABLE_INFO, os, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> deleteOs(String ids) {
        String[] idArr = ids.split(",");
        tableService.delete(TABLE_INFO, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getOsList(String data, WebPageInfo webPageInfo) {
        PageInfo<Map<String, Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
        return ResultVO.getSuccess(pageInfo);
    }

    @Override
    public ResultVO<Object> validUrl(String url) {
        Condition condition = Condition.creatCriteria().andEqual("OS_URL", url).build();
        Long i = baseMapper.count(OS_SYS_OS, condition);
        if (i > 0L) {
            return ResultVO.getFailed("访问地址不能重复");
        }
        return ResultVO.getSuccess();
    }
}
