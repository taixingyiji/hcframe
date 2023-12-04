package com.taixingyiji.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.taixingyiji.user.module.userinfo.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 */
@RestController
@RequestMapping("dept")
@Api(tags = "机构管理")
public class DeptController {

    final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @PostMapping()
    @ApiOperation(value = "新增dept", notes = "将自动传承key-value对象模式即可")
    @RequiresPermissions(value = {"system:userManage:orgManage:add"})
    public ResultVO<Object> addDept(@RequestParam Map<String, Object> org) {
        return deptService.addDept(org);
    }

    @PutMapping("/{version}")
    @ApiOperation(value = "更新dept")
    @RequiresPermissions(value = {"system:userManage:orgManage:edit"})
    public ResultVO<Integer> updateDept(@RequestParam Map<String, Object> org, @PathVariable Integer version) {
        return deptService.updateDept(org, version);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除dept", notes = "删除后职位也会被删除")
    @RequiresPermissions(value = {"system:userManage:orgManage:delete"})
    public ResultVO<Object> deleteDept(@PathVariable String ids) {
        return deptService.deleteDept(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取机构列表", notes = "删除后职位也会被删除")
    @RequiresPermissions(value = {"orgManage","empowerOrg","system:empower:org:list","system:userManage:orgManage:list"},logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getDeptList(String data, WebPageInfo webPageInfo,String code) {
        return deptService.getDeptList(data, webPageInfo,code);
    }

    @GetMapping("/tree")
    @ApiOperation(value = "获取机构树")
    public ResultVO<List<Map<String, Object>>> getDeptTree() {
        return deptService.getDeptTree();
    }
}
