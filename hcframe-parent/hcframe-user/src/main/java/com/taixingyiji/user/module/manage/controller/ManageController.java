package com.taixingyiji.user.module.manage.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.user.module.manage.service.ManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lhc
 * @date 2021-02-05
 */
@Api(tags = "馆内用户管理")
@RequestMapping("manage")
@RestController
public class ManageController {

    final
    ManageService manageService;

    public ManageController(ManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/{name}")
    public ResultVO<String> getName(@PathVariable String name) {
        return ResultVO.getSuccess("Hello!" + name);
    }

    @PostMapping()
    @LogAnno(operateType = "新增用户信息", moduleName = "系统管理-用户管理-馆内用户管理")
    @ApiOperation(value = "新增用户信息", notes = "将自动传承ey-value对象模式即可")
    @RequiresPermissions(value = {"system:userManage:innerUser:add"})
    public ResultVO<Map<String, Object>> addUser(@RequestParam Map<String, Object> user) {
        return manageService.addUser(user);
    }

    @PutMapping("/{version}")
    @LogAnno(operateType = "更新用户信息", moduleName = "系统管理-用户管理-馆内用户管理")
    @ApiOperation(value = "更新用户信息")
    @RequiresPermissions(value = {"system:userManage:innerUser:edit"})
    public ResultVO<Map<String,Object>> updateUser(@RequestParam Map<String, Object> user, @PathVariable Integer version) {
        return manageService.updateUser(user, version);
    }

    @DeleteMapping("/{ids}")
    @LogAnno(operateType = "删除用户信息", moduleName = "系统管理-用户管理-馆内用户管理")
    @ApiOperation(value = "删除用户（逻辑删除）", notes = "删除后职位也会被删除")
    @RequiresPermissions(value = {"system:userManage:innerUser:delete"})
    public ResultVO<Integer> deleteUser(@PathVariable String ids) {
        return manageService.deleteUser(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取用户列表")
    @RequiresPermissions(value = {"userinfo", "system:userManage:innerUser:list", "system:empower:innerUser:list", "empowerUser"}, logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo, String orgId) {
        return manageService.getUserList(data, webPageInfo, orgId);
    }

    @PutMapping("disable/{version}")
    @LogAnno(operateType = "用户启用禁用", moduleName = "系统管理-用户管理-馆内用户管理")
    @ApiOperation(value = "启用/禁用", notes = "用户启用禁用")
    @RequiresPermissions(value = {"system:userManage:innerUser:enabled"})
    public ResultVO<Map<String,Object>> disable(Boolean enabled, String userId, @PathVariable Integer version) {
        return manageService.disable(enabled, userId, version);
    }

    @PutMapping("/resetPassword/{version}")
    @LogAnno(operateType = "重置密码", moduleName = "系统管理-用户管理-馆内用户管理")
    @ApiOperation(value = "重置密码")
    @RequiresPermissions(value = {"system:userManage:innerUser:resetPassword"})
    public ResultVO<Map<String,Object>> resetPassword(String userId, @PathVariable Integer version) {
        return manageService.resetPassword(userId, version);
    }

    @PutMapping("changePassword")
    @LogAnno(operateType = "修改密码", moduleName = "系统管理-用户管理-馆内用户管理")
    @ApiOperation(value = "修改密码", notes = "用户输入原密码和新密码")
    public ResultVO<Map<String,Object>> changePassword(String pwd, String npwd, String npwd2) {
        return manageService.changePassword(pwd, npwd, npwd2);
    }

    @GetMapping("/sync")
    public ResultVO<Object> sync() {
        return manageService.sync();
    }

    @GetMapping("getUserPost")
    @ApiOperation(value = "获取用户副岗信息", notes = "用户输入原密码和新密码")
    public ResultVO<Object> getUserPost(String userId) {
        return manageService.getUserPost(userId);
    }
}
