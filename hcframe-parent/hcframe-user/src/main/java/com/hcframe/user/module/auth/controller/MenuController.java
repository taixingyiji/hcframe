package com.hcframe.user.module.auth.controller;

import java.util.List;
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
import com.hcframe.user.module.auth.service.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * @author wewe
 * @date 2021年4月13日
 * @description 角色管理
 */
@RestController
@Api(tags = "功能级权限管理")
@RequestMapping("menu")
public class MenuController {
	
	@Autowired MenuService menuService;
	
	@PostMapping()
    @ApiOperation(value = "新增功能级权限", notes = "给后台传key-value对象模式即可")
    public ResultVO<Object> addMenu(@RequestParam Map<String, Object> data) {
        return menuService.addMenu(data);
    }
	
	@DeleteMapping()
    @ApiOperation(value = "删除功能级权限", notes = "删除后关联表数据也会被删除")
    public ResultVO<Object> deleteMenu(@RequestParam List<Long> ids) {
        return menuService.deleteMenu(ids);
    }
	
	@PutMapping("/{version}")
    @ApiOperation(value = "更新功能级权限")
    public ResultVO<Integer> updateMenu(@RequestParam Map<String, Object> data, @PathVariable Integer version) {
        return menuService.updateMenu(data, version);
    }
	
	@GetMapping()
    @ApiOperation(value = "查询功能级权限列表")
    public ResultVO<PageInfo<Map<String, Object>>> getMenuList(String data, WebPageInfo webPageInfo) {
        return menuService.getMenuList(data, webPageInfo);
    }
	
	@PostMapping()
    @ApiOperation(value = "角色授权", notes = "roleIds,menuIds,中间用逗号连接")
    public ResultVO<Object> addRoleMenu(@RequestParam List<Long> roleIds,@RequestParam List<Long> menuIds) {
        return menuService.addRoleMenu(roleIds, menuIds);
    }
	
	@PostMapping()
    @ApiOperation(value = "删除角色授权", notes = "ids,中间用逗号连接")
    public ResultVO<Object> deleteRoleMenu(@RequestParam String ids) {
        return menuService.deleteRoleMenu(ids);
    }

}
