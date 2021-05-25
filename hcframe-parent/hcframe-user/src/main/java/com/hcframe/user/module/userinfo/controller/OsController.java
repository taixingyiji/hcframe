package com.hcframe.user.module.userinfo.controller;

import java.util.Map;

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
import com.hcframe.base.module.log.annotation.LogAnno;
import com.hcframe.user.module.userinfo.service.OrgService;
import com.hcframe.user.module.userinfo.service.OsService;
import com.hcframe.user.module.userinfo.service.TitleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("os")
@Api(tags = "应用系统管理")
public class OsController {

    final OsService osService;

    public OsController(OsService osService) {
        this.osService = osService;
    }

    @GetMapping("/{oscode}")
    @ApiOperation(value = "校验系统编码是否存在", notes = "将自动传承key-value对象模式即可")
    public ResultVO<Object> checkExistOs(@PathVariable String oscode) {
        return osService.checkExistOs(oscode);
    }

    @GetMapping("validUrl")
    @ApiOperation(value = "校验系统编码是否存在", notes = "将自动传承key-value对象模式即可")
    public ResultVO<Object> validUrl(String url) {
        return osService.validUrl(url);
    }

    @PostMapping()
    @LogAnno(operateType = "新增系统信息", moduleName = "系统管理-权限管理-系统信息管理")
    @ApiOperation(value = "新增系统", notes = "将自动传承key-value对象模式即可")
    public ResultVO<Object> addOs(@RequestParam Map<String, Object> os) {
        return osService.addOs(os);
    }

    @PutMapping("/{version}")
    @LogAnno(operateType = "更新系统信息", moduleName = "系统管理-权限管理-系统信息管理")
    @ApiOperation(value = "更新系统信息")
    public ResultVO<Integer> updateOs(@RequestParam Map<String, Object> os, @PathVariable Integer version) {
        return osService.updateOs(os, version);
    }

    @DeleteMapping("/{ids}")
    @LogAnno(operateType = "删除系统信息", moduleName = "系统管理-权限管理-系统信息管理")
    @ApiOperation(value = "删除系统信息")
    public ResultVO<Object> deleteOs(@PathVariable String ids) {
        return osService.deleteOs(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取系统信息列表")
    public ResultVO<PageInfo<Map<String, Object>>> getOsList(String data, WebPageInfo webPageInfo) {
        return osService.getOsList(data, webPageInfo);
    }

}
