package com.hcframe.user.module.auth.service;

import com.hcframe.base.common.ResultVO;

/**
 * @author lhc
 * @version 1.0
 * @className RoleUserService
 * @date 2021年04月15日 9:09 上午
 * @description 描述
 */
public interface RoleUserService {
    ResultVO<Object> roleUserBind(Long userId, String roleId);

    ResultVO<Object> roleGroupBind(Long userId, String groupId);
}
