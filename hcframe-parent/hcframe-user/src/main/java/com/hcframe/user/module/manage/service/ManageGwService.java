package com.hcframe.user.module.manage.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

/**
 * @author wewe
 * @date 2021年4月19日
 * @description 馆外用户管理接口
 */
public interface ManageGwService {
	
	ResultVO<Map<String,Object>> addUser(Map<String, Object> user);

    ResultVO<Integer> updateUser(Map<String, Object> user, Integer version);

    ResultVO<Integer> deleteUser(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo);

    ResultVO<Integer> resetPassword(String userId, Integer version);

    ResultVO<Integer> disable(Boolean enabled, String userId, Integer version);

}