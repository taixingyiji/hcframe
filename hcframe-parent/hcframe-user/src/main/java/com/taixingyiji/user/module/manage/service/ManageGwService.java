package com.taixingyiji.user.module.manage.service;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

import java.util.Map;

/**
 * @author wewe
 * @date 2021年4月19日
 * @description 馆外用户管理接口
 */
public interface ManageGwService {

	ResultVO<Map<String,Object>> addUser(Map<String, Object> user);

    ResultVO<Integer> updateUser(Map<String, Object> user, Integer version);

    ResultVO<Integer> deleteUser(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo,String orgId);

    ResultVO<Integer> resetPassword(String userId, Integer version);

    ResultVO<Integer> disable(Boolean enabled, String userId, Integer version);

    ResultVO<Object> valid(String loginName);
}
