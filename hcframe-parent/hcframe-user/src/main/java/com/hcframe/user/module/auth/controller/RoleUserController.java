package com.hcframe.user.module.auth.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.user.module.auth.service.RoleUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhc
 * @version 1.0
 * @className RoleUserController
 * @date 2021年04月15日 9:09 上午
 * @description 描述
 */
@RestController
@RequestMapping("/roleUser")
@Api(tags="用户授权")
public class RoleUserController {

    final RoleUserService roleUserService;

    public RoleUserController(RoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @PostMapping("role")
    public ResultVO<Object> roleUserBind(Long userId, String roleId) {
        return roleUserService.roleUserBind(userId,roleId);
    }

    @PostMapping("roleGroup")
    public ResultVO<Object> roleGroupBind(Long userId, String groupId) {
        return roleUserService.roleGroupBind(userId,groupId);
    }
}
