package com.taixingyiji.user.module.userinfo.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

public interface TitleService {
	
	    ResultVO<Object> checkExistTitle(String titlecode);

	    ResultVO<Object> addTitle(Map<String, Object> title);

	    ResultVO<Integer> updateTitle(Map<String, Object> title, Integer version);

	    ResultVO<Object> deleteTitle(String ids);

	    ResultVO<PageInfo<Map<String, Object>>> getTitleList(String data, WebPageInfo webPageInfo);
}
