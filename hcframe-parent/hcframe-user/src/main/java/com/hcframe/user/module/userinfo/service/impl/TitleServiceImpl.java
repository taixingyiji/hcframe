package com.hcframe.user.module.userinfo.service.impl;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.module.userinfo.service.TitleService;

@Service
public class TitleServiceImpl implements TitleService{
	
	 private static final String TITLE_ID = "TITLE_ID";
	    private static final String OS_SYS_TITLE = "OS_SYS_TITLE";
	    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(OS_SYS_TITLE).tablePk(TITLE_ID).build();

	    final BaseMapper baseMapper;

	    final TableService tableService;

	    public TitleServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
	                          TableService tableService) {
	        this.baseMapper = baseMapper;
	        this.tableService = tableService;
	    }

	    @Override
		public ResultVO<Object> addTitle(Map<String, Object> title) {
	        return ResultVO.getSuccess(tableService.saveWithDate(TABLE_INFO, title));
	    }

	    @Override
	    public ResultVO<Integer> updateTitle(Map<String, Object> title, Integer version) {
	        return tableService.updateWithDate(TABLE_INFO,title,version);
	    }

	    @Override
	    @Transactional(rollbackFor = Exception.class)
	    public  ResultVO<Object> deleteTitle(String ids) {
	        String[] idArr = ids.split(",");
	        tableService.delete(TABLE_INFO, ids);
	        return ResultVO.getSuccess();
	    }

	    @Override
	    public  ResultVO<PageInfo<Map<String, Object>>> getTitleList(String data, WebPageInfo webPageInfo) {
	        PageInfo<Map<String,Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
	        return ResultVO.getSuccess(pageInfo);
	    }

}
