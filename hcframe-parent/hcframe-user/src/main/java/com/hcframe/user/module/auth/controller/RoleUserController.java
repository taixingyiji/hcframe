package com.hcframe.user.module.auth.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.log.annotation.LogAnno;
import com.hcframe.redis.RedisUtil;
import com.hcframe.user.module.auth.service.RoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    final RedisUtil redisUtil;

    public RoleUserController(RoleUserService roleUserService,
                              RedisUtil redisUtil) {
        this.roleUserService = roleUserService;
        this.redisUtil = redisUtil;
    }

    @PostMapping("role")
    @LogAnno(operateType="用户角色绑定",moduleName="系统管理-权限管理-用户授权")
    @ApiOperation(value = "用户角色绑定")
    public ResultVO<Object> roleUserBind(String userId, String roleIds) {
        redisUtil.del("auth");
        return roleUserService.roleUserBind(userId,roleIds);
    }

    @GetMapping("role")
    @ApiOperation(value = "获取用户角色")
    public ResultVO<Object> getUserRole(String userId) {
        return roleUserService.getUserRole(userId);
    }

    @PostMapping("roleGroup")
    @LogAnno(operateType="用户角色组绑定",moduleName="系统管理-权限管理-用户授权")
    @ApiOperation(value = "用户角色组绑定")
    public ResultVO<Object> roleGroupBind(String userId, String groupIds) {
        redisUtil.del("auth");
        return roleUserService.roleGroupBind(userId,groupIds);
    }

    @GetMapping("roleGroup")
    @ApiOperation(value = "获取用户的角色组")
    public ResultVO<Object> getUserGroup(String userId) {
        return roleUserService.getUserGroup(userId);
    }
}
