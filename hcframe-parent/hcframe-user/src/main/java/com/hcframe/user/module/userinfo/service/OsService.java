package com.hcframe.user.module.userinfo.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

public interface OsService {
	
	    ResultVO<Object> checkExistOs(String oscode);

	    ResultVO<Object> addOs(Map<String, Object> os);

	    ResultVO<Integer> updateOs(Map<String, Object> os, Integer version);

	    ResultVO<Object> deleteOs(String ids);

	    ResultVO<PageInfo<Map<String, Object>>> getOsList(String data, WebPageInfo webPageInfo);
}
