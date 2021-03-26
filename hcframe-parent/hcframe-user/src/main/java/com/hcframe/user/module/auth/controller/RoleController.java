package com.hcframe.user.module.auth.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.user.module.auth.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleController
 * @date 2021年03月26日 9:40 上午
 * @description 角色管理
 */
@RestController
public class RoleController {

    final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping()
    @ApiOperation(value = "新增role", notes = "给后台传key-value对象模式即可")
    public ResultVO<Object> addRole(@RequestParam Map<String,Object> role) {
        return roleService.addRole(role);
    }

    @PutMapping("/{version}")
    @ApiOperation(value = "更新role")
    public ResultVO<Integer> updateRole(@RequestParam Map<String,Object> role,@PathVariable Integer version) {
        return roleService.updateRole(role,version);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除role", notes = "删除后关联表数据也会被删除")
    public ResultVO<Object> deleteOrg(@PathVariable String ids) {
        return roleService.deleteRole(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取机构列表")
    public ResultVO<PageInfo<Map<String,Object>>> getOrgList(String data, WebPageInfo webPageInfo) {
        return roleService.getRoleList(data, webPageInfo);
    }
}
