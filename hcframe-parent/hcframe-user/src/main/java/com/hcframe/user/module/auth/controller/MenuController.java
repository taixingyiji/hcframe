package com.hcframe.user.module.auth.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.log.annotation.LogAnno;
import com.hcframe.redis.RedisUtil;
import com.hcframe.user.module.auth.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * @author wewe
 * @date 2021年4月13日
 * @description 角色管理
 */
@RestController("userMenuController")
@Api(tags = "功能级权限管理")
@RequestMapping("menu")
public class MenuController {

	@Autowired MenuService menuService;

    final RedisUtil redisUtil;

    public MenuController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

	@PostMapping("add")
	@LogAnno(operateType="新增功能权限",moduleName="系统管理-权限管理-功能权限管理-新增功能权限")
    @ApiOperation(value = "新增功能级权限", notes = "给后台传key-value对象模式即可")
    public ResultVO<Object> addMenu(@RequestParam Map<String, Object> data) {
        return menuService.addMenu(data);
    }

	@PostMapping("delete")
	@LogAnno(operateType="删除功能权限",moduleName="系统管理-权限管理-功能权限管理-删除功能权限")
    @ApiOperation(value = "删除功能级权限", notes = "删除后关联表数据也会被删除")
    public ResultVO<Object> deleteMenu(@RequestParam List<Long> ids) {
        redisUtil.del("auth");
        return menuService.deleteMenu(ids);
    }

	@PutMapping("/{version}")
	@LogAnno(operateType="更新功能权限",moduleName="系统管理-权限管理-功能权限管理-更新功能权限")
    @ApiOperation(value = "更新功能级权限")
    public ResultVO<Integer> updateMenu(@RequestParam Map<String, Object> data, @PathVariable Integer version) {
        redisUtil.del("auth");
        return menuService.updateMenu(data, version);
    }

	@GetMapping("list")
    @ApiOperation(value = "查询功能级权限列表")
    public ResultVO<PageInfo<Map<String, Object>>> getMenuList(String data, WebPageInfo webPageInfo) {
        return menuService.getMenuList(data, webPageInfo);
    }

	@PostMapping("addRole")
	@LogAnno(operateType="角色授权",moduleName="系统管理-授权管理-角色授权")
    @ApiOperation(value = "角色授权", notes = "roleId,menuIds,中间用逗号连接")
    public ResultVO<Object> addRoleMenu(@RequestParam Long roleId,@RequestParam(required=false) List<String> menuIds) {
        redisUtil.del("auth");
        return menuService.addRoleMenu(roleId, menuIds);
    }

	@GetMapping("tree")
    @ApiOperation(value = "功能级权限树，根节点是业务系统")
    public ResultVO<Object> getMenuTree() {
        return menuService.getMenuTree();
    }

	@GetMapping("selected")
    @ApiOperation(value = "获取当前角色已选中节点")
    public ResultVO<Object> getSelectedMenu(@RequestParam Long roleId) {
        return menuService.getSelectedMenu(roleId);
    }

	@PostMapping("checkPath")
    @ApiOperation(value = "校验PATH是否唯一", notes = "")
    public ResultVO<Object> checkPath(@RequestParam Map<String, Object> data) {
        return menuService.checkPath(data);
    }

	@GetMapping("oslist")
    @ApiOperation(value = "获取系统信息列表")
    public ResultVO<Object> getOsList() {
        return menuService.getOsList();
    }

}
