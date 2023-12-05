package com.taixingyiji.user.module.userinfo.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

public interface OsService {

    ResultVO<Object> checkExistOs(String oscode);

    ResultVO<Object> addOs(Map<String, Object> os);

    ResultVO<Map<String,Object>> updateOs(Map<String, Object> os, Integer version);

    ResultVO<Object> deleteOs(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getOsList(String data, WebPageInfo webPageInfo);

    ResultVO<Object> validUrl(String url);
}
