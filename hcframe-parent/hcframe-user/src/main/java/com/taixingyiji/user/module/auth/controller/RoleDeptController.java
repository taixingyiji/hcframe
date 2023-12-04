package com.taixingyiji.user.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.user.module.auth.service.RoleDeptService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Li Xinlei
 * @version 1.0
 * @className RoleDeptController
 * @date 2021-4-20 17:15:09 
 * @description 描述
 */


@RestController
@RequestMapping("/roleDept")
@Api(tags="机构授权")
public class RoleDeptController {

    final RoleDeptService roleDeptService;

    public RoleDeptController(RoleDeptService roleDeptService) {
        this.roleDeptService = roleDeptService;
    }

    @PostMapping("role")
    public ResultVO<Object> roleDeptBind(String deptId, String roleIds) {
        return roleDeptService.roleDeptBind(deptId,roleIds);
    }

    @GetMapping("role")
    public ResultVO<Object> getDeptRole(String deptId) {
        return roleDeptService.getDeptRole(deptId);
    }

    @PostMapping("roleGroup")
    public ResultVO<Object> roleGroupBind(String deptId, String groupIds) {
        return roleDeptService.roleGroupBind(deptId,groupIds);
    }

    @GetMapping("roleGroup")
    public ResultVO<Object> getDeptGroup(String deptId) {
        return roleDeptService.getDeptGroup(deptId);
    }
}
