package com.hcframe.user.module.auth.service;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleService
 * @date 2021年03月26日 9:42 上午
 * @description 描述
 */
public interface RoleService {
    ResultVO<Object> addRole(Map<String, Object> role);

    ResultVO<Integer> updateRole(Map<String, Object> role, Integer version);

    ResultVO<Object> deleteRole(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getRoleList(String data, WebPageInfo webPageInfo);

    ResultVO<Object> validCode(String code);

    ResultVO<List<Map<String, Object>>> getAll();
}
