package com.hcframe.user.module.auth.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.user.module.auth.service.RoleGroupServie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleGroupController
 * @date 2021年04月02日 10:11 上午
 * @description 描述
 */
@RestController
@Api(tags = "角色组管理")
@RequestMapping("roleGroup")
public class RoleGroupController {

    final RoleGroupServie roleGroupServie;

    public RoleGroupController(RoleGroupServie roleGroupServie) {
        this.roleGroupServie = roleGroupServie;
    }

    @PostMapping()
    @ApiOperation(value = "新增角色组", notes = "给后台传key-value对象模式即可")
    public ResultVO<Map<String,Object>>  addRole(@RequestParam Map<String, Object> roleGroup) {
        return roleGroupServie.add(roleGroup);
    }

    @PutMapping("/{version}")
    @ApiOperation(value = "更新角色组")
    public ResultVO<Integer> updateRole(@RequestParam Map<String, Object> roleGroup, @PathVariable Integer version) {
        return roleGroupServie.update(roleGroup, version);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除角色组", notes = "删除后关联表数据也会被删除")
    public ResultVO<Integer> deleteOrg(@PathVariable String ids) {
        return roleGroupServie.delete(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取角色组列表")
    public ResultVO<PageInfo<Map<String, Object>>> getOrgList(String data, WebPageInfo webPageInfo) {
        return roleGroupServie.getList(data, webPageInfo);
    }

    @PostMapping("bind")
    @ApiOperation(value = "绑定角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "角色组ID",required = true),
            @ApiImplicitParam(name = "roleIds", value = "角色ID数组",required = true)
    })
    public ResultVO<Object> bind(Integer groupId, String roleIds) {
        return roleGroupServie.bind(groupId, roleIds);
    }

    @GetMapping("getRoles")
    @ApiOperation(value = "绑定角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "角色组ID",required = true),
    })
    public ResultVO<Object> getRoles(Integer groupId) {
        return roleGroupServie.getRoles(groupId);
    }

}
