package com.hcframe.user.module.userinfo.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

public interface LogService {
	ResultVO<PageInfo<Map<String, Object>>> getLogList(String data, WebPageInfo webPageInfo);
}