package com.hcframe.user.module.manage.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.user.module.manage.service.ManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lhc
 * @date 2021-02-05
 */
@Api(tags = "用户管理")
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
        return ResultVO.getSuccess("Hello!"+name);
    }

    @PostMapping()
    @ApiOperation(value = "新增用户信息", notes = "将自动传承ey-value对象模式即可")
    public ResultVO<Map<String,Object>> addUser(@RequestParam Map<String,Object> user) {
        return manageService.addUser(user);
    }

    @PutMapping("/{version}")
    @ApiOperation(value = "更新用户信息")
    public ResultVO<Integer> updateUser(@RequestParam Map<String,Object> user,@PathVariable Integer version) {
        return manageService.updateUser(user,version);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除用户（逻辑删除）", notes = "删除后职位也会被删除")
    public ResultVO<Integer> deleteUser(@PathVariable String ids) {
        return manageService.deleteUser(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取用户列表", notes = "删除后职位也会被删除")
    public ResultVO<PageInfo<Map<String,Object>>> getUserList(String data, WebPageInfo webPageInfo) {
        return manageService.getUserList(data, webPageInfo);
    }

    @PutMapping("disable/{version}")
    @ApiOperation(value = "启用/禁用",notes = "用户启用禁用")
    public ResultVO<Integer> disable(Boolean enabled,String userId,@PathVariable Integer version) {
        return manageService.disable(enabled,userId,version);
    }

    @PutMapping("/resetPassword/{version}")
    @ApiOperation(value = "重置密码",notes = "用户启用禁用")
    public ResultVO<Integer> resetPassword(String userId,@PathVariable Integer version) {
        return manageService.resetPassword(userId,version);
    }

    @GetMapping("/sync")
    public ResultVO<Object> sync() {
        return manageService.sync();
    }
}
