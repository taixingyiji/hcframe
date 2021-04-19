package com.hcframe.user.module.manage.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.user.module.manage.service.ManageGwService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author wewe
 * @date 2021年4月19日
 * @description 馆外用户增删改查
 */
@Api(tags = "馆外用户管理")
@RequestMapping("manage-gw")
@RestController
public class ManageGwController {
	
	@Autowired ManageGwService service;
	
	@PostMapping()
    @ApiOperation(value = "新增用户信息", notes = "将自动传承ey-value对象模式即可")
    public ResultVO<Map<String,Object>> addUser(@RequestParam Map<String,Object> user) {
        return service.addUser(user);
    }
	
	@PutMapping("/{version}")
    @ApiOperation(value = "更新用户信息")
    public ResultVO<Integer> updateUser(@RequestParam Map<String,Object> user,@PathVariable Integer version) {
        return service.updateUser(user,version);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除用户（逻辑删除）", notes = "删除后职位也会被删除")
    public ResultVO<Integer> deleteUser(@PathVariable String ids) {
        return service.deleteUser(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取用户列表", notes = "删除后职位也会被删除")
    public ResultVO<PageInfo<Map<String,Object>>> getUserList(String data, WebPageInfo webPageInfo) {
        return service.getUserList(data, webPageInfo);
    }

    @PutMapping("disable/{version}")
    @ApiOperation(value = "启用/禁用",notes = "用户启用禁用")
    public ResultVO<Integer> disable(Boolean enabled,String userId,@PathVariable Integer version) {
        return service.disable(enabled,userId,version);
    }

    @PutMapping("/resetPassword/{version}")
    @ApiOperation(value = "重置密码",notes = "用户启用禁用")
    public ResultVO<Integer> resetPassword(String userId,@PathVariable Integer version) {
        return service.resetPassword(userId,version);
    }


}
