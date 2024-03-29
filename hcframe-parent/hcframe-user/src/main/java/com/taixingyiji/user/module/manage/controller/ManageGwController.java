package com.taixingyiji.user.module.manage.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.user.module.manage.service.ManageGwService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author wewe
 * @date 2021年4月19日
 * @description 馆外用户增删改查
 */
@Api(tags = "馆外用户管理")
@RequestMapping("manage-gw")
@RestController
public class ManageGwController {

	@Autowired
    ManageGwService service;

	@PostMapping()
	@LogAnno(operateType="新增馆外用户",moduleName="系统管理-用户管理-馆外用户信息管理")
    @ApiOperation(value = "新增用户信息", notes = "将自动传承ey-value对象模式即可")
    @RequiresPermissions(value = {"system:userManage:outerUser:add"})
    public ResultVO<Map<String,Object>> addUser(@RequestParam Map<String,Object> user) {
        return service.addUser(user);
    }

	@PutMapping("/{version}")
	@LogAnno(operateType="编辑馆外用户信息",moduleName="系统管理-用户管理-馆外用户信息管理")
    @ApiOperation(value = "更新用户信息")
    @RequiresPermissions(value = {"system:userManage:outerUser:edit"})
    public ResultVO<Map<String,Object>> updateUser(@RequestParam Map<String,Object> user,@PathVariable Integer version) {
        return service.updateUser(user,version);
    }

    @DeleteMapping("/{ids}")
    @LogAnno(operateType="删除馆外用户",moduleName="系统管理-用户管理-馆外用户信息管理")
    @ApiOperation(value = "删除用户（逻辑删除）", notes = "删除后职位也会被删除")
    @RequiresPermissions(value = {"system:userManage:outerUser:delete"})
    public ResultVO<Integer> deleteUser(@PathVariable String ids) {
        return service.deleteUser(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取用户列表" )
    @RequiresPermissions(value = {"outerUserInfo","empowerOuterUser","system:empower:outerUser:list","system:userManage:outerUser:list"},logical = Logical.OR)
    public ResultVO<PageInfo<Map<String,Object>>> getUserList(String data, WebPageInfo webPageInfo,String orgId) {
        return service.getUserList(data, webPageInfo,orgId);
    }

    @PutMapping("disable/{version}")
    @LogAnno(operateType="启用/禁用馆外用户",moduleName="系统管理-用户管理-馆外用户信息管理")
    @ApiOperation(value = "启用/禁用馆外用户",notes = "用户启用禁用")
    @RequiresPermissions(value = {"system:userManage:outerUser:enable"})
    public ResultVO<Map<String,Object>> disable(Boolean enabled,String userId,@PathVariable Integer version) {
        return service.disable(enabled,userId,version);
    }

    @PutMapping("/resetPassword/{version}")
    @LogAnno(operateType="馆外用户重置密码",moduleName="系统管理-用户管理-馆外用户信息管理")
    @ApiOperation(value = "馆外用户重置密码",notes = "用户启用禁用")
    @RequiresPermissions(value = {"system:userManage:outerUser:resetPassword"})
    public ResultVO<Map<String,Object>> resetPassword(String userId,@PathVariable Integer version) {
        return service.resetPassword(userId,version);
    }

    @GetMapping("valid")
    @ApiOperation(value = "校验用户名")
    public ResultVO<Object> valid(String loginName) {
        return service.valid(loginName);
    }

}
