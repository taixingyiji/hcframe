package com.hcframe.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.user.module.userinfo.service.OrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lhc
 */
@RestController
@RequestMapping("org")
@Api(tags = "机构管理")
public class OrgController {

    final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @PostMapping()
    @ApiOperation(value = "新增org", notes = "将自动传承key-value对象模式即可")
    public ResultVO<Object> addOrg(@RequestParam Map<String,Object> org) {
        return orgService.addOrg(org);
    }

    @PutMapping("/{version}")
    @ApiOperation(value = "更新org")
    public ResultVO<Integer> updateOrg(@RequestParam Map<String,Object> org,@PathVariable Integer version) {
        return orgService.updateOrg(org,version);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除org", notes = "删除后职位也会被删除")
    public ResultVO<Object> deleteOrg(@PathVariable String ids) {
        return orgService.deleteOrg(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取机构列表", notes = "删除后职位也会被删除(职位暂未实现)")
    public ResultVO<PageInfo<Map<String,Object>>> getOrgList(String data, WebPageInfo webPageInfo,String parentId) {
        return orgService.getOrgList(data, webPageInfo , parentId);
    }

    @GetMapping(value = "tree")
    @ApiOperation(value = "获取机构树")
    public ResultVO<Object> getOrgTree() {
        return ResultVO.getSuccess(orgService.getOrgTree());
    }

    @GetMapping(value = "format")
    public ResultVO<Object> formatOrg() {
        return orgService.getFormat();
    }
}
