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
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.module.userinfo.service.LogService;


@Service
public class LogServiceImpl implements LogService{
	
	 private static final String LOG_ID = "LOG_ID";
	 private static final String OS_SYS_TITLE = "GB_LOGS";
	 private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(OS_SYS_TITLE).tablePk(LOG_ID).build();

	    final BaseMapper baseMapper;

	    final TableService tableService;

	    public LogServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
	                          TableService tableService) {
	        this.baseMapper = baseMapper;
	        this.tableService = tableService;
	    }
	    
	   

	    @Override
	    public  ResultVO<PageInfo<Map<String, Object>>> getLogList(String data, WebPageInfo webPageInfo) {
	        PageInfo<Map<String,Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
	        return ResultVO.getSuccess(pageInfo);
	    }

}