package com.taixingyiji.user.module.manage.service;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

import java.util.Map;

public interface ManageService {
    ResultVO<Map<String,Object>> addUser(Map<String, Object> user);

    ResultVO<Map<String,Object>> updateUser(Map<String, Object> user, Integer version);

    ResultVO<Integer> deleteUser(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo, String orgId);

    ResultVO<Map<String,Object>> resetPassword(String userId, Integer version);

    ResultVO<Map<String,Object>> disable(Boolean enabled, String userId, Integer version);

    ResultVO<Object> sync();

	ResultVO<Map<String,Object>> changePassword(String pwd, String npwd, String npwd2);

    ResultVO<Object> getUserPost(String userId);
}
