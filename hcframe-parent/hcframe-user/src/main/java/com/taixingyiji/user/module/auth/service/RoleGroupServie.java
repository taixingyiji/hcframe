package com.taixingyiji.user.module.auth.service;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleGroupServie
 * @date 2021年04月02日 10:09 上午
 * @description 描述
 */
public interface RoleGroupServie {


    ResultVO<Map<String,Object>>  add(Map<String, Object> roleGroup);

    ResultVO<Integer> update(Map<String, Object> roleGroup, Integer version);

    ResultVO<Integer> delete(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getList(String data, WebPageInfo webPageInfo);

    ResultVO<Object> bind(Integer roleGroupId, String roleIds);

    ResultVO<Object> getRoles(Integer groupId);

    ResultVO<Object> getAll();
}
