package com.taixingyiji.user.module.userinfo.service.impl;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.cache.CacheService;
import com.taixingyiji.base.module.cache.impl.TableCache;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.user.module.userinfo.service.TitleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class TitleServiceImpl implements TitleService {

    private static final String TITLE_ID = "TITLE_ID";
    private static final String OS_SYS_TITLE = "OS_SYS_TITLE";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(OS_SYS_TITLE).tablePk(TITLE_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    final CacheService tableCache;

    public TitleServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                            TableService tableService,
                            @Qualifier(TableCache.TABLE) CacheService cacheService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
        this.tableCache = cacheService;
    }

    @Override
    public ResultVO<Object> checkExistTitle(String titlecode) {
        Condition condition = Condition.creatCriteria().andEqual("TITLE_CODE", titlecode).andEqual("DELETED", 1).build();
        Long i = baseMapper.count(OS_SYS_TITLE, condition);
        if (i > 0L) {
            return ResultVO.getFailed("职称编码不能重复");
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Object> addTitle(Map<String, Object> title) {
        tableCache.delete("title");
        return ResultVO.getSuccess(tableService.saveWithDate(TABLE_INFO, title));
    }

    @Override
    public ResultVO<Map<String,Object>> updateTitle(Map<String, Object> title, Integer version) {
        tableCache.delete("title");
        return tableService.updateWithDate(TABLE_INFO, title, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> deleteTitle(String ids) {
        tableCache.delete("title");
        tableService.delete(TABLE_INFO, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getTitleList(String data, WebPageInfo webPageInfo) {
        PageInfo<Map<String, Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
        return ResultVO.getSuccess(pageInfo);
    }

}
