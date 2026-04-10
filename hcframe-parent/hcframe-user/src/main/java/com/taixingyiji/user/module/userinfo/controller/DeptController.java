package com.taixingyiji.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.user.module.userinfo.service.DeptService;
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
public class DeptController {

    final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @PostMapping()
    @RequiresPermissions(value = {"system:userManage:orgManage:add"})
    public ResultVO<Object> addDept(@RequestParam Map<String, Object> org) {
        return deptService.addDept(org);
    }

    @PutMapping("/{version}")
    @RequiresPermissions(value = {"system:userManage:orgManage:edit"})
    public ResultVO<Map<String,Object>> updateDept(@RequestParam Map<String, Object> org, @PathVariable Integer version) {
        return deptService.updateDept(org, version);
    }

    @DeleteMapping("/{ids}")
    @RequiresPermissions(value = {"system:userManage:orgManage:delete"})
    public ResultVO<Object> deleteDept(@PathVariable String ids) {
        return deptService.deleteDept(ids);
    }

    @GetMapping()
    @RequiresPermissions(value = {"orgManage","empowerOrg","system:empower:org:list","system:userManage:orgManage:list"},logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getDeptList(String data, WebPageInfo webPageInfo,String code) {
        return deptService.getDeptList(data, webPageInfo,code);
    }

    @GetMapping("/tree")
    public ResultVO<List<Map<String, Object>>> getDeptTree() {
        return deptService.getDeptTree();
    }
}
