package com.hcframe.user.module.manage.service;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

import java.util.Map;

public interface ManageService {
    ResultVO<Map<String,Object>> addUser(Map<String, Object> user);

    ResultVO<Integer> updateUser(Map<String, Object> user, Integer version);

    ResultVO<Integer> deleteUser(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo, String orgId);

    ResultVO<Integer> resetPassword(String userId, Integer version);

    ResultVO<Integer> disable(Boolean enabled, String userId, Integer version);

    ResultVO<Object> sync();

	ResultVO<Integer> changePassword(String pwd, String npwd, String npwd2);
}
