package com.taixingyiji.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.user.module.userinfo.service.OrgService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lhc
 */
@RestController
@RequestMapping("org")
public class OrgController {

    final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @PostMapping()
    public ResultVO<Object> addOrg(@RequestParam Map<String,Object> org) {
        return orgService.addOrg(org);
    }

    @PutMapping("/{version}")
    public ResultVO<Map<String,Object>> updateOrg(@RequestParam Map<String,Object> org,@PathVariable Integer version) {
        return orgService.updateOrg(org,version);
    }

    @DeleteMapping("/{ids}")
    public ResultVO<Object> deleteOrg(@PathVariable String ids) {
        return orgService.deleteOrg(ids);
    }

    @GetMapping()
    public ResultVO<PageInfo<Map<String,Object>>> getOrgList(String data, WebPageInfo webPageInfo,String parentId) {
        return orgService.getOrgList(data, webPageInfo , parentId);
    }

    @GetMapping(value = "tree")
    public ResultVO<Object> getOrgTree() {
        return ResultVO.getSuccess(orgService.getOrgTree());
    }

    @GetMapping(value = "format")
    public ResultVO<Object> formatOrg() {
        return orgService.getFormat();
    }
}
