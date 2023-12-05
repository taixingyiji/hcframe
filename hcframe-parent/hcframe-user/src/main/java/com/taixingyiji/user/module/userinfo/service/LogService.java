package com.taixingyiji.user.module.userinfo.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

public interface LogService {
	ResultVO<PageInfo<Map<String, Object>>> getLogList(String data, WebPageInfo webPageInfo);
	
	ResultVO<PageInfo<Map<String, Object>>> getLoginLogList(String data, WebPageInfo webPageInfo);
}
