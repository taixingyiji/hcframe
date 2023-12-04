package com.taixingyiji.user.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.user.module.auth.service.RoleUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResultVO<Object> roleUserBind(String userId, String roleIds) {
        return roleUserService.roleUserBind(userId,roleIds);
    }

    @GetMapping("role")
    public ResultVO<Object> getUserRole(String userId) {
        return roleUserService.getUserRole(userId);
    }

    @PostMapping("roleGroup")
    public ResultVO<Object> roleGroupBind(String userId, String groupIds) {
        return roleUserService.roleGroupBind(userId,groupIds);
    }

    @GetMapping("roleGroup")
    public ResultVO<Object> getUserGroup(String userId) {
        return roleUserService.getUserGroup(userId);
    }
}
